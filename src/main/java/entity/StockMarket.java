package entity;

import data_access.IStockDataAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A singleton class representing the stock market
 */

// TODO: lock the stock market when updating stock prices
public class StockMarket {

    // TODO: add a method to get all stocks for market search panel
    // thread-safe Singleton instance
    private static volatile StockMarket instance = null;

    private Map<String, Stock> stocks = new ConcurrentHashMap<>();
    private IStockDataAccess dataAccess;
    private boolean initialized = false;

    // use read-write lock to ensure stock data is not read during update
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private StockMarket() {
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
    public synchronized void initialize(IStockDataAccess dataAccess) {
        if (this.initialized) {
            throw new IllegalStateException("StockMarket is already initialized.");
        }
        this.dataAccess = dataAccess;
        this.initialized = true;
    }

    public Optional<Stock> getStock(String ticker) {
        lock.readLock().lock();
        return Optional.ofNullable(stocks.get(ticker));
    }

    public List<Stock> getStocks() {
        lock.readLock().lock();
        return new ArrayList<>(stocks.values());
    }

    /**
     * Update the stock prices in the stock market
     */
    // TODO: set interval for updating stock prices
    public void updateStockPrices() {
        lock.writeLock().lock();
        if (dataAccess == null) {
            throw new IllegalStateException("StockMarket has not been initialized with a data access object.");
        }
        this.stocks = dataAccess.getStocks();
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            String ticker = entry.getKey();
            String company = entry.getValue().getCompany();
            String industry = entry.getValue().getIndustry();
            double price = entry.getValue().getPrice();
            // create a new map entry if stock does not exist, and then update price
            stocks.computeIfAbsent(ticker, k -> new Stock(ticker, company, industry, price)).updatePrice(price);
        }
    }
}
