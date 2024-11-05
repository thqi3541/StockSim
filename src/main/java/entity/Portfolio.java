package entity;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private Map<String, UserStock> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

    public double getTotalValue() {
        double result = 0.0;

        for (UserStock stock : stocks.values()) {
            result += stock.getCurrentTotalPrice();
        }

        return result;
    }

    public int getStockQuantity(String ticker) {
        return stocks.get(ticker).getQuantity();
    }

    // TODO: add a method to update portfolio
}
