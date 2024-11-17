package utility;

import data_access.StockDataAccessInterface;
import entity.Stock;
import view.view_events.UpdateStockEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Singleton utility class that manages the stock market state and updates.
 */
public class StockMarket implements AutoCloseable {
    private static final long UPDATE_INTERVAL_MS = 300000; // 5 minutes
    private static volatile StockMarket instance;
    private final Map<String, Stock> stocks = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final ScheduledExecutorService updateScheduler;
    private final List<String> configuredTickers;
    private StockDataAccessInterface dataAccess;
    private volatile boolean initialized = false;
    private volatile boolean shutdownRequested = false;

    private StockMarket() {
        this.updateScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "StockMarket-Updater");
            t.setDaemon(true);
            return t;
        });
        this.configuredTickers = loadConfiguredTickers();
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

    private List<String> loadConfiguredTickers() {
        List<String> tickers = new ArrayList<>();
        try {
            // First try to load from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/tickers.txt");

            // If not found in resources, try to load from file system
            if (inputStream == null) {
                Path tickersPath = Paths.get("src", "main", "config", "tickers.txt");
                if (!Files.exists(tickersPath)) {
                    throw new IOException("Ticker file not found at: " + tickersPath);
                }
                inputStream = Files.newInputStream(tickersPath);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String ticker = line.trim();
                    if (!ticker.isEmpty()) {
                        tickers.add(ticker);
                    }
                }
            }
            System.out.println("Loaded " + tickers.size() + " tickers from configuration");
        } catch (IOException e) {
            System.err.println("Failed to read ticker file: " + e.getMessage());
            e.printStackTrace();
        }
        return Collections.unmodifiableList(tickers);
    }

    public synchronized CompletableFuture<Void> initialize(StockDataAccessInterface dataAccess) {
        if (this.initialized) {
            System.out.println("StockMarket already initialized");
            return CompletableFuture.completedFuture(null);
        }

        if (dataAccess == null) {
            throw new IllegalArgumentException("DataAccess cannot be null");
        }

        this.dataAccess = dataAccess;
        System.out.println("Initializing StockMarket...");

        return refreshStockData()
                .thenAccept(success -> {
                    if (success) {
                        this.initialized = true;
                        scheduleUpdates();
                        System.out.println("StockMarket initialization complete");
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
                System.out.println("Fetching stock data...");
                Map<String, Stock> newStocks = dataAccess.getStocks();

                lock.writeLock().lock();
                try {
                    stocks.clear();
                    stocks.putAll(newStocks);
                    // Convert Map values to List for the event
                    List<Stock> stockList = new ArrayList<>(stocks.values());
                    ViewManager.Instance().broadcastEvent(new UpdateStockEvent(stockList));
                } finally {
                    lock.writeLock().unlock();
                }

                System.out.println("Successfully updated " + stocks.size() + " stocks");
                return true;
            } catch (Exception e) {
                System.err.println("Error refreshing stock data: " + e.getMessage());
                return false;
            }
        });
    }

    private void scheduleUpdates() {
        if (!shutdownRequested) {
            updateScheduler.scheduleAtFixedRate(
                    () -> refreshStockData().join(),
                    UPDATE_INTERVAL_MS,
                    UPDATE_INTERVAL_MS,
                    TimeUnit.MILLISECONDS
            );
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

    public Map<String, Stock> getStocks() {
        lock.readLock().lock();
        try {
            return new HashMap<>(stocks);
        } finally {
            lock.readLock().unlock();
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

    public List<String> getConfiguredTickers() {
        return configuredTickers;
    }

    @Override
    public void close() {
        System.out.println("Shutting down StockMarket...");
        shutdownRequested = true;

        if (updateScheduler != null) {
            updateScheduler.shutdown();
        }

        stocks.clear();
        initialized = false;
        System.out.println("StockMarket shutdown complete");
    }
}
