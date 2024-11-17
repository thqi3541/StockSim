package data_access;

import entity.Stock;
import utility.exceptions.RateLimitExceededException;

import java.util.Map;

public interface StockDataAccessInterface {

    /**
     * Get the prices of all stocks
     *
     * @return a hashmap with the stock ticker as the key and the Stock entity as the value.
     * It should contain all stocks in the database.
     */
    Map<String, Stock> getStocks() throws RateLimitExceededException;
}
