package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Portfolio {

    private final Map<String, UserStock> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

    /**
     * Constructor for Portfolio class.
     * Portfolio contains all the stocks a user is holding: tickers -> quantities(position).
     *
     * @param stocks a map of tickers and the stocks user hold
     */
    public Portfolio(Map<String, UserStock> stocks) {
        this.stocks = new HashMap<>(stocks);
    }

    public double getTotalValue() {
        double result = 0.0;

        for (UserStock stock : stocks.values()) {
            result += stock.getCurrentMarketValue();
        }

        return result;
    }

    public Optional<UserStock> getUserStock(String ticker) {
        return Optional.ofNullable(stocks.get(ticker));
    }

    public void addStock(UserStock userStock) {
        stocks.put(userStock.getStock().getTicker(), userStock);
    }

    public void removeStock(UserStock userStock) {
        stocks.remove(userStock.getStock().getTicker());
    }

    public void addTransaction(Transaction transaction) {
        // TODO: placeholder function, the logic should be changed
        // after transaction is created, portfolio accept it and call the user stock to handle it
        // 1. check if the stock is in the portfolio
        // 2. if not, create a new user stock
        // 3. if is, update the quantity and check if it is 0 and remove it
        UserStock userStock = stocks.get(transaction.ticker());
        if (userStock == null) {
            return;
        }
        userStock.addTransaction(transaction);
    }
}
