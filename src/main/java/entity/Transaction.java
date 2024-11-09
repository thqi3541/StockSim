package entity;

import java.util.Date;

public record Transaction(Date timestamp, String ticker, int quantity, double price) {

    public double getTotalPrice() {
        return price * quantity;
    }
}
