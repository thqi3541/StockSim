package entity;

import java.util.Date;

public class Transaction {

    private final Date timestamp;
    private final String ticker;
    private final int quantity;
    private final double price;

    public Transaction(Date timestamp, String ticker, int quantity, double price) {
        this.timestamp = timestamp;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}
