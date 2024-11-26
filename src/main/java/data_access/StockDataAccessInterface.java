package data_access;

import entity.Stock;
import utility.exceptions.RateLimitExceededException;

import java.util.Map;
import utility.exceptions.RateLimitExceededException;

public interface StockDataAccessInterface {

    /**
     * Get all stock information
     *
     * @return a hashmap with the stock ticker as the key and the Stock entity as the value.
     * It should contain all stocks in the database.
     */
    Map<String, Stock> getStocks() throws RateLimitExceededException;

    /**
     * Get updated market price for all stocks
     *
     * @return a hashmap with the stock ticker as the key and the updated market price as the value.
     */
    Map<String, Double> getUpdatedPrices() throws RateLimitExceededException;
}
