package entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A class representing a user's portfolio.
 */
public class Portfolio {

  private Map<String, UserStock> userStocks;

  /**
   * Default constructor for creating an empty portfolio.
   */
  public Portfolio() {
    this.userStocks = new HashMap<>();
  }

  /**
   * Create a portfolio with the given stocks.
   *
   * @param userStocks the stocks in the portfolio
   * @throws IllegalArgumentException if userStocks is null
   */
  public Portfolio(Map<String, UserStock> userStocks) {
    if (userStocks == null) {
      throw new IllegalArgumentException("userStocks cannot be null.");
    }
    this.userStocks = new HashMap<>(userStocks);
  }

  /**
   * Get the total value of all stocks in the portfolio.
   *
   * @return the total value of all stocks
   */
  public double getTotalValue() {
    return userStocks.values().stream().mapToDouble(UserStock::getMarketValue).sum();
  }

  /**
   * Retrieve a specific UserStock by its ticker.
   *
   * @param ticker the ticker symbol of the stock
   * @return an Optional containing the UserStock if found, or an empty Optional otherwise
   * @throws IllegalArgumentException if ticker is null
   */
  public Optional<UserStock> getUserStock(String ticker) {
    if (ticker == null) {
      throw new IllegalArgumentException("ticker cannot be null.");
    }
    return Optional.ofNullable(userStocks.get(ticker));
  }

  /**
   * Retrieve all UserStock objects in the portfolio as a Map.
   *
   * @return a map of all UserStock objects keyed by ticker
   */
  public Map<String, UserStock> getUserStocks() {
    return new HashMap<>(userStocks); // Return a copy to maintain immutability
  }

  /**
   * Retrieve all UserStock objects in the portfolio as a Collection.
   *
   * @return a collection of all UserStock objects
   */
  public Collection<UserStock> getAllUserStocks() {
    return userStocks.values();
  }

  /**
   * Adds or updates a stock in the portfolio.
   *
   * @param userStock the UserStock to add or update
   * @throws IllegalArgumentException if userStock is null
   */
  public void addUserStock(UserStock userStock) {
    if (userStock == null || userStock.getStock() == null) {
      throw new IllegalArgumentException("userStock or its Stock cannot be null.");
    }
    userStocks.put(userStock.getStock().getTicker(), userStock);
  }

  /**
   * Removes a stock from the portfolio.
   *
   * @param userStock the UserStock to remove
   * @throws IllegalArgumentException if userStock is null
   */
  public void removeUserStock(UserStock userStock) {
    if (userStock == null || userStock.getStock() == null) {
      throw new IllegalArgumentException("userStock or its Stock cannot be null.");
    }
    userStocks.remove(userStock.getStock().getTicker());
  }

  /**
   * Checks if the portfolio is empty.
   *
   * @return true if the portfolio contains no stocks, false otherwise
   */
  public boolean isEmpty() {
    return userStocks.isEmpty();
  }
}
