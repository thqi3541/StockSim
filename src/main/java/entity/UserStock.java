package entity;

public class UserStock {

    private final Stock stock;
    private double cost;
    private int quantity;

    public UserStock(Stock stock, double cost, int quantity) {
        this.stock = stock;
        this.cost = cost;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public double getCost() {
        return cost;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculate the total cost spent on this stock.
     * Not sure what this method does.
     * @return the total value of the stock in the stock market
     */
    public double getPurchasedTotalCost() {
        return cost * quantity;
    }

    /**
     * Calculate the total market value of this stock in the stock market.
     * This method is used to rank users.
     * @return the current market value of this stock in the stock market
     */
    public double getCurrentMarketValue() {
        return stock.getPrice() * quantity;
    }

    /**
     * Update the cost basis and quantity of the stock, when a transaction is successfully made.
     * We update a new cost with a weighted average cost method.
     * @param price: the purchase price of additional shares of the stock
     *             calculate the weighted average cost of the stock
     *             Precondition: price >= 0
     *             Precondition: cost >= 0
     * @param quantity: the new quantity of the stock
     *             quantity could be positive(Buy) or negative(Sell)
     */
    public void updateUserStock(double price, int quantity) {
        this.cost = (this.cost * this.quantity + price * quantity) / (quantity + this.quantity);
        this.quantity += quantity;
    }
}
