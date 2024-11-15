package entity;

import java.util.Date;

/**
 * A class representing a transaction
 */
public record Transaction(
        Date timestamp,
        Stock stock,
        int quantity,
        double price,
        String type
) {
    public Date getTimestamp() {
        return timestamp;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}
