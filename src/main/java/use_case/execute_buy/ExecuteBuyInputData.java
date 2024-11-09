package use_case.execute_buy;

import entity.*;

public class ExecuteBuyInputData {

    private final User user;
    private final String ticker;
    private final int quantity;

    public ExecuteBuyInputData(User user, String ticker, int quantity) {
        this.user = user;
        this.ticker = ticker;
        this.quantity = quantity;
    }

    public User getUser() { return user; }
    public String getTicker() { return ticker; }
    public int getQuantity() { return quantity; }

}
