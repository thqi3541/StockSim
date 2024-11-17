package entity;

import data_access.StockDataAccessInterface;
import utility.ViewManager;
import utility.exceptions.RateLimitExceededException;
import view.view_events.UpdateStockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A singleton class representing the stock market
 */

public class StockMarket {

    // market information update interval in milliseconds
    private static final long INITIAL_UPDATE_MARKET_INTERVAL = 1000; // initial interval in milliseconds
    private static final long UPDATE_INTERVAL_ADJUSTMENT_RATE = 500; // interval adjustment rate in milliseconds
    private static final int ROUNDS_WITHOUT_RATE_LIMIT_TO_DECREASE = 5; // number of rounds without rate limit

    // thread-safe Singleton instance
    private static volatile StockMarket instance = null;

    // use read-write lock to ensure stock data is not read during update
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile long currentUpdateInterval = INITIAL_UPDATE_MARKET_INTERVAL;
    private int roundsWithoutRateLimit = 0;
    private Map<String, Stock> stocks = new ConcurrentHashMap<>();
    private StockDataAccessInterface dataAccess;
    private boolean initialized = false;
    private ScheduledExecutorService scheduler;

    private StockMarket() {
        startUpdatingStockPrices();
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

    // initialize the stock market with data access object
    public synchronized void initialize(StockDataAccessInterface dataAccess) {
        if (this.initialized) {
            throw new IllegalStateException("StockMarket is already initialized.");
        }
        this.dataAccess = dataAccess;
        this.initialized = true;
    }

    public Optional<Stock> getStock(String ticker) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(stocks.get(ticker));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Stock> getStocks() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(stocks.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Update the stock information periodically based on API rate limit
     * <p>
     * The update rate initially INITIAL_UPDATE_MARKET_INTERVAL
     * When the API rate limit exceeds, increase update interval by UPDATE_INTERVAL_ADJUSTMENT_RATE
     * If API rate limit not exceeded in consecutive ROUNDS_WITHOUT_RATE_LIMIT_TO_DECREASE,
     * decrease update interval by UPDATE_INTERVAL_ADJUSTMENT_RATE until reaching INITIAL_UPDATE_MARKET_INTERVAL
     * </p>
     */
    public void updateStocks() {
        lock.writeLock().lock();
        try {
            if (dataAccess == null) {
                throw new IllegalStateException("StockMarket has not been initialized with a data access object.");
            }

            // retrieve stock information from data access object
            this.stocks = dataAccess.getStocks();
            for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
                String ticker = entry.getKey();
                String company = entry.getValue().getCompany();
                String industry = entry.getValue().getIndustry();
                double price = entry.getValue().getPrice();
                stocks.computeIfAbsent(ticker, k -> new Stock(ticker, company, industry, price)).updatePrice(price);
            }

            // if no exception, increment the rounds counter
            roundsWithoutRateLimit++;
            // check if it's time to reduce the interval
            if (roundsWithoutRateLimit >= ROUNDS_WITHOUT_RATE_LIMIT_TO_DECREASE &&
                    currentUpdateInterval > INITIAL_UPDATE_MARKET_INTERVAL) {
                currentUpdateInterval = Math.max(currentUpdateInterval - UPDATE_INTERVAL_ADJUSTMENT_RATE,
                        INITIAL_UPDATE_MARKET_INTERVAL);
                roundsWithoutRateLimit = 0; // reset counter after adjustment
                restartScheduler(); // restart the scheduler with new interval
            }

            // broadcast stock update to view
            ViewManager.Instance().broadcastEvent(new UpdateStockEvent(getStocks()));

        } catch (RateLimitExceededException e) {
            // on rate limit, increase the update interval and reset the rounds counter
            currentUpdateInterval += UPDATE_INTERVAL_ADJUSTMENT_RATE;
            roundsWithoutRateLimit = 0;
            restartScheduler(); // restart scheduler with new interval
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Starts a background thread to update stock prices at fixed intervals.
     */
    public synchronized void startUpdatingStockPrices() {
        if (scheduler != null && !scheduler.isShutdown()) {
            throw new IllegalStateException("Stock price updating is already running.");
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::updateStocks, 0, currentUpdateInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops the periodic stock price updates.
     */
    public synchronized void stopUpdatingStockPrices() {
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
            scheduler = null;
        }
    }

    /**
     * Restarts the scheduler with the current update interval.
     */
    private synchronized void restartScheduler() {
        stopUpdatingStockPrices();
        startUpdatingStockPrices();
    }
}
