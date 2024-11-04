package entity;

import data_access.IStockDataAccess;

import java.util.Map;
import java.util.HashMap;

public class StockMarket {

    // A thread-safe Singleton instance
    private static volatile StockMarket instance = null;

    private Map<String, Stock> stocks = new HashMap<>();
    private IStockDataAccess dataAccess;

    private StockMarket() {}

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
    public void initialize(IStockDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public Stock getStock(String ticker) {
        return stocks.get(ticker);
    }

    public void updateStockPrices() {
        Map<String, Double> prices = dataAccess.getStockPrices();
        for (Map.Entry<String, Double> entry : prices.entrySet()) {
            String ticker = entry.getKey();
            double price = entry.getValue();
            if (stocks.containsKey(ticker)) {
                stocks.get(ticker).updatePrice(price);
            } else {
                stocks.put(ticker, new Stock(ticker, price));
            }
        }
    }
}
