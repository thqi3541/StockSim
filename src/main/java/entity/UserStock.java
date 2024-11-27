package entity;

/**
 * A class representing a stock owned by a user.
 */
public class UserStock {

    private final Stock stock;
    private double avgCost;
    private int quantity;

    // BSON constructor
    public UserStock() {
        // This constructor is required for BSON deserialization
        this.stock = null;
        this.avgCost = 0;
        this.quantity = 0;
    }

    /**
     * Constructor for UserStock class.
     *
     * @param stock    the stock object (must not be null)
     * @param avgCost  the average cost of the stock
     * @param quantity the quantity of the stock owned by the user
     */
    public UserStock(Stock stock, double avgCost, int quantity) {
        this.stock = stock;
        this.avgCost = avgCost;
        this.quantity = quantity;
    }

    // Getters
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
     * Calculates the total cost spent on this stock.
     *
     * @return the total cost spent on this stock
     */
    public double getTotalCost() {
        return avgCost * quantity;
    }

    /**
     * Calculates the total market value of this stock in the stock market.
     *
     * @return the current market value of this stock in the stock market
     */
    public double getMarketValue() {
        return stock.getMarketPrice() * quantity;
    }

    /**
     * Updates the average cost and quantity of the stock.
     *
     * @param price    the execution price of the stock (must be non-negative)
     * @param quantity the quantity of the stock to add (can be negative to represent selling)
     */
    public void updateUserStock(double price, int quantity) {
        this.avgCost = (this.avgCost * this.quantity + price * quantity) /
                (quantity + this.quantity);
        this.quantity += quantity;
    }

    @Override
    public String toString() {
        return "UserStock{" +
                "stock=" + stock.getTicker() +
                ", avgCost=" + avgCost +
                ", quantity=" + quantity +
                ", totalCost=" + getTotalCost() +
                ", marketValue=" + getMarketValue() +
                '}';
    }
}
