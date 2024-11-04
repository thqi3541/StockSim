package entity;

public class UserStock {

    private final Stock stock;
    private double purchasedPrice;
    private int quantity;

    public UserStock(Stock stock, double purchasedPrice, int quantity) {
        this.stock = stock;
        this.purchasedPrice = purchasedPrice;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public double getPurchasedPrice() {
        return purchasedPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPurchasedTotalPrice() {
        return purchasedPrice * quantity;
    }

    public double getCurrentTotalPrice() {
        return stock.getPrice() * quantity;
    }

    public void updateStock(double price, int quantity) {
        // calculate new purchased price as weighted average
        this.purchasedPrice = (this.purchasedPrice * this.quantity + price * quantity) / (quantity + this.quantity);
        this.quantity += quantity;
    }
}
