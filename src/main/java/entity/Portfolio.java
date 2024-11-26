package entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A class representing a user's portfolio
 */
public class Portfolio {

    private final Map<String, UserStock> userStocks;

    public Portfolio() {
        this.userStocks = new HashMap<>();
    }

    /**
     * Create a portfolio with the given stocks
     *
     * @param userStocks the stocks in the portfolio
     */
    public Portfolio(Map<String, UserStock> userStocks) {
        this.userStocks = new HashMap<>(userStocks);
    }

    /**
     * Get the value of all stocks in the portfolio
     *
     * @return the value of all stocks in the portfolio
     */
    public double getTotalValue() {
        return userStocks.values().stream()
                .mapToDouble(UserStock::getMarketValue)
                .sum();
    }

    /**
     * Retrieve a specific UserStock by its ticker.
     *
     * @param ticker the ticker of the stock
     * @return an Optional containing the UserStock if found, or an empty Optional otherwise
     */
    public Optional<UserStock> getUserStock(String ticker) {
        return Optional.ofNullable(userStocks.get(ticker));
    }

    /**
     * Retrieve all UserStock objects in the portfolio.
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
     */
    public void addUserStock(UserStock userStock) {
        userStocks.put(userStock.getStock().getTicker(), userStock);
    }

    /**
     * Removes a stock from the portfolio.
     *
     * @param userStock the UserStock to remove
     */
    public void removeUserStock(UserStock userStock) {
        userStocks.remove(userStock.getStock().getTicker());
    }

    /** When a transaction is made
     * this method updates the portfolio with the new stock and quantity
     * If the stock is already in the portfolio, it updates the  and cost
     * If the stock is not in the portfolio, it adds the stock to the portfolio
     *
     * @param stock        the stock the user buys
     * @param quantity     the quantity the user buys
     * @param currentPrice the current executionPrice of the stock
     */
    private void updatePortfolio(Stock stock, int quantity, double currentPrice) {
        // find the stock in the portfolio
        UserStock userStock = userStocks.get(stock.getTicker());
        if (userStock != null) {
            // update the stock in the portfolio
            userStock.updateUserStock(currentPrice, quantity);
        } else {
            // add the stock to the portfolio
            userStocks.put(stock.getTicker(), new UserStock(stock, currentPrice, quantity));
        }
    }
}