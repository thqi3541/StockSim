package data_access;

import entity.Stock;
import java.io.IOException;
import java.util.Map;
import utility.exceptions.RateLimitExceededException;

public interface StockDataAccessInterface {

  /**
   * Get the prices of all stocks
   *
   * @return a hashmap with the stock ticker as the key and the Stock entity as the value. It should
   * contain all stocks in the database.
   */
  // TODO: should this be a list instead of a map? as we are not using the key anywhere
  Map<String, Stock> getStocks() throws RateLimitExceededException, IOException;
}
