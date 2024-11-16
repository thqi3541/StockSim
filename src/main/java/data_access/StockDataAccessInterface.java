package data_access;

import entity.Stock;

import java.util.Map;

public interface StockDataAccessInterface {

    /**
     * Get the prices of all stocks
     *
     * @return a hashmap with the stock ticker as the key and the Stock entity as the value.
     * It should contain all stocks in the database.
     */
    Map<String, Stock> getStocks();
}
