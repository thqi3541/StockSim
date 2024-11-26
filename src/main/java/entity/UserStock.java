package entity;

/**
 * A class representing a stock owned by a user
 */
public class UserStock {

    private final Stock stock;
    private double avgCost;
    private int quantity;

    /**
     * Constructor for UserStock class
     *
     * @param stock:    the stock object
     * @param avgCost:  the average cost of the stock
     * @param quantity: the quantity of the stock owned by user
     */
    public UserStock(Stock stock, double avgCost, int quantity) {
        this.stock = stock;
        this.avgCost = avgCost;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public double getAvgCost() {
        return avgCost;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculate the total cost spent on this stock
     *
     * @return the total cost spent on this stock
     */
    public double getTotalCost() {
        return avgCost * quantity;
    }

    /**
     * Calculate the total market value of this stock in the stock market
     *
     * @return the current market value of this stock in the stock market
     */
    public double getMarketValue() {
        return stock.getMarketPrice() * quantity;
    }

    /**
     * Update the average cost and quantity of the stock
     *
     * @param price:    the executionPrice of the stock
     * @param quantity: the quantity of the stock
     */
    public void updateUserStock(double price, int quantity) {
        this.avgCost = (this.avgCost * this.quantity + price * quantity) / (quantity + this.quantity);
        this.quantity += quantity;
    }
}
