package entity;

/**
 * A stock entity
 */
public class Stock {

    // TODO: need to add more fields like company name, sector, etc.
    private final String ticker;
    private double price;

    public Stock(String ticker, double price) {
        this.ticker = ticker;
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Update the current market price of the stock
     *
     * @param price the new price
     */
    public void updatePrice(double price) {
        this.price = price;
    }
}
