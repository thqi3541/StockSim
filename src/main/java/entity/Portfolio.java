package entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A class representing a user's portfolio
 */
public class Portfolio {

    private final Map<String, UserStock> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

    /**
     * Create a portfolio with the given stocks
     *
     * @param stocks the stocks in the portfolio
     */
    public Portfolio(Map<String, UserStock> stocks) {
        this.stocks = new HashMap<>(stocks);
    }

    /**
     * Get the value of all stocks in the portfolio
     *
     * @return the value of all stocks in the portfolio
     */
    public double getTotalValue() {
        return stocks.values().stream()
                .mapToDouble(UserStock::getCurrentMarketValue)
                .sum();
    }

    /**
     * Retrieve a specific UserStock by its ticker.
     *
     * @param ticker the ticker of the stock
     * @return an Optional containing the UserStock if found, or an empty Optional otherwise
     */
    public Optional<UserStock> getUserStock(String ticker) {
        return Optional.ofNullable(stocks.get(ticker));
    }

    /**
     * Retrieve all UserStock objects in the portfolio.
     *
     * @return a collection of all UserStock objects
     */
    public Collection<UserStock> getAllStocks() {
        return stocks.values();
    }

    /**
     * Adds or updates a stock in the portfolio.
     *
     * @param userStock the UserStock to add or update
     */
    public void addStock(UserStock userStock) {
        stocks.put(userStock.getStock().getTicker(), userStock);
    }

    /**
     * Removes a stock from the portfolio.
     *
     * @param userStock the UserStock to remove
     */
    public void removeStock(UserStock userStock) {
        stocks.remove(userStock.getStock().getTicker());
    }
}