package entity;

import java.util.Date;

/**
 * A class representing a transaction
 */
public record Transaction(
        Date timestamp,
        String ticker,
        int quantity,
        double executionPrice,
        String type
) {

    /**
     * Getter to retrieve transaction date
     *
     * @return date of transaction
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Getter to retrieve transaction ticker
     *
     * @return ticker of transaction
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * Getter to retrieve transaction quantity
     *
     * @return number of stocks involved in the transaction
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter to retrieve transaction stock executionPrice
     *
     * @return executionPrice of a single stock from transaction
     */
    public double getExecutionPrice() {
        return executionPrice;
    }

    /**
     * Getter to retrieve type of transaction (i.e. buy or sell)
     *
     * @return the transaction type
     */
    public String getType() {
        return type;
    }
}
