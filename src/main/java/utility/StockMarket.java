package utility;

import data_access.StockDataAccessInterface;
import data_access.StockDataAccessObject;
import entity.Stock;
import view.view_events.UpdateStockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Singleton utility class that manages the stock market state and updates.
 */
public class StockMarket implements AutoCloseable {
    private static final long DEFAULT_UPDATE_INTERVAL_MS = 120000; // 2 minutes
    private static volatile StockMarket instance;
    private final Map<String, Stock> stocks;
    private final ReadWriteLock lock;
    private final ScheduledExecutorService updateScheduler;
    private final long updateIntervalMs;
    private StockDataAccessInterface dataAccess;
    private volatile boolean initialized = false;
    private volatile boolean shutdownRequested = false;

    private StockMarket() {
        this(DEFAULT_UPDATE_INTERVAL_MS);
    }

    private StockMarket(long updateIntervalMs) {
        this.stocks = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.updateIntervalMs = updateIntervalMs;
        this.updateScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "StockMarket-Updater");
            t.setDaemon(true);
            return t;
        });
    }

    public static StockMarket Instance() {
        if (instance == null) {
            synchronized (StockMarket.class) {
                if (instance == null) {
                    instance = new StockMarket();
                }
            }
        }
        return instance;
    }

    public synchronized CompletableFuture<Void> initialize(StockDataAccessInterface dataAccess) {
        if (this.initialized) {
            return CompletableFuture.completedFuture(null);
        }

        if (dataAccess == null) {
            throw new IllegalArgumentException("DataAccess cannot be null");
        }

        this.dataAccess = dataAccess;

        return refreshStockData()
                .thenAccept(success -> {
                    if (success) {
                        this.initialized = true;
                        scheduleUpdates();
                        broadcastStockUpdate();
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Failed to initialize StockMarket: " + e.getMessage());
                    return null;
                });
    }

    private CompletableFuture<Boolean> refreshStockData() {
        if (shutdownRequested) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Stock> newStocks = ((StockDataAccessObject) dataAccess)
                        .getStocks(null); // Progress callback removed for cleaner logs

                updateStocks(newStocks);
                return true;
            } catch (Exception e) {
                System.err.println("Error refreshing stock data: " + e.getMessage());
                return false;
            }
        });
    }

    private void updateStocks(Map<String, Stock> newStocks) {
        if (newStocks == null || newStocks.isEmpty()) {
            return;
        }

        lock.writeLock().lock();
        try {
            stocks.clear();
            stocks.putAll(newStocks);
            broadcastStockUpdate();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void broadcastStockUpdate() {
        List<Stock> stockList;
        lock.readLock().lock();
        try {
            stockList = new ArrayList<>(stocks.values());
        } finally {
            lock.readLock().unlock();
        }

        if (!stockList.isEmpty()) {
            ViewManager.Instance().broadcastEvent(new UpdateStockEvent(stockList));
        }
    }

    private void scheduleUpdates() {
        if (!shutdownRequested) {
            updateScheduler.scheduleAtFixedRate(
                    () -> {
                        try {
                            refreshStockData().join();
                        } catch (Exception e) {
                            System.err.println("Error during scheduled update: " + e.getMessage());
                        }
                    },
                    updateIntervalMs,
                    updateIntervalMs,
                    TimeUnit.MILLISECONDS
            );
        }
    }

    public List<Stock> getStocksList() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(stocks.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Stock> getStock(String ticker) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(stocks.get(ticker));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void close() {
        shutdownRequested = true;

        if (updateScheduler != null) {
            updateScheduler.shutdown();
            try {
                if (!updateScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    updateScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        lock.writeLock().lock();
        try {
            stocks.clear();
        } finally {
            lock.writeLock().unlock();
        }
        initialized = false;
    }
}
