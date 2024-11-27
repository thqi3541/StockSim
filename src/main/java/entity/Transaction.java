package entity;

import java.util.Date;
import java.util.Objects;

/**
 * A record representing a transaction.
 */
public record Transaction(
        Date timestamp,
        String ticker,
        int quantity,
        double executionPrice,
        String type
) {

    /**
     * Constructs a new Transaction with validation for inputs.
     *
     * @param timestamp      the date and time of the transaction
     * @param ticker         the stock ticker symbol
     * @param quantity       the number of stocks involved in the transaction
     * @param executionPrice the execution price per stock
     * @param type           the type of transaction (e.g., "buy", "sell")
     * @throws IllegalArgumentException if any field is invalid
     */
    public Transaction {
        Objects.requireNonNull(timestamp, "Timestamp cannot be null.");
        Objects.requireNonNull(ticker, "Ticker cannot be null or empty.");
        Objects.requireNonNull(type,
                               "Transaction type cannot be null or empty.");

        if (ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be empty.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }
        if (executionPrice < 0) {
            throw new IllegalArgumentException(
                    "Execution price cannot be negative.");
        }
        if (!type.equalsIgnoreCase("buy") && !type.equalsIgnoreCase("sell")) {
            throw new IllegalArgumentException(
                    "Transaction type must be either 'buy' or 'sell'.");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction[timestamp=%s, ticker=%s, quantity=%d, executionPrice=%.2f, type=%s]",
                timestamp, ticker, quantity, executionPrice, type
        );
    }
}
