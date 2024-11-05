package entity;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private final Map<String, UserStock> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

    // Constructor that initializes Portfolio with a map of tickers and their respective quantities
    public Portfolio(Map<String, Integer> stocks) {
        this.stocks = new HashMap<>();
        StockMarket stockMarket = StockMarket.Instance();

        stocks.forEach((ticker, quantity) -> {
            Stock stock = stockMarket.getStock(ticker);
            if (stock != null) {
                double price = stock.getPrice();
                if (price > 0) {
                    this.stocks.put(ticker, new UserStock(stock, price, quantity));
                } else {
                    System.err.println("Invalid price for stock: " + ticker);
                }
            } else {
                System.err.println("No stock found for ticker: " + ticker);
            }
        });
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
