package entity;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private final Map<String, UserStock> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

    /** Constructor for Portfolio class.
     * Portfolio contains all the stocks a user is holding: tickers -> quantities(position).
     * @param stocks a map of tickers and the number of shares the user holds.     *
     */
    // TODO: change the input to Map<String, Stock/UserStock> stocks
    // 异常处理有待商榷
    public Portfolio(Map<String, Integer> stocks) {
        this.stocks = new HashMap<>();
        StockMarket stockMarket = StockMarket.Instance();

        stocks.forEach((ticker, quantity) -> {
            // decompose Optional<Stock>
            stockMarket.getStock(ticker).ifPresentOrElse(
                stock -> {
                    double price = stock.getPrice();
                    if (price > 0) {
                        this.stocks.put(ticker, new UserStock(stock, price, quantity));
                    } else {
                        throw new IllegalArgumentException("Invalid price for stock: " + ticker);
                    }
                },
                () -> {
                    throw new IllegalArgumentException("No stock found for ticker: " + ticker);
                }
            );
        });
    }

    public double getTotalValue() {
        double result = 0.0;

        for (UserStock stock : stocks.values()) {
            result += stock.getCurrentMarketValue();
        }

        return result;
    }

    public int getStockQuantity(String ticker) {
        return stocks.get(ticker).getQuantity();
    }

    // TODO: add a method to update portfolio
}
