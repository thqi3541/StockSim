package entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Portfolio {

    private final Map<String, UserStock> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

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
}
