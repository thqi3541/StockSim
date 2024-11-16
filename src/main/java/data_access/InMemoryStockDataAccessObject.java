package data_access;

import entity.Stock;

import java.util.Map;

public class InMemoryStockDataAccessObject implements IStockDataAccess {
    private final Map<String, Stock> stocks;

    public InMemoryStockDataAccessObject() {
        this.stocks = new java.util.HashMap<>();
    }

    @Override
    public Map<String, Stock> getStocks() {
        stocks.put("XXXX", new Stock("XXXX", "X Company", "Technology", 100.0));
        stocks.put("YYYY", new Stock("YYYY", "Y Company", "Semiconductors", 200.0));
        stocks.put("ZZZZ", new Stock("ZZZZ", "Z Company", "Pharmaceuticals", 300.0));
        return stocks;
    }
}
