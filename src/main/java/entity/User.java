package entity;

import javax.swing.text.Position;

public class User {

    private final String username;
    private final String password;
    private final double balance;
    private Portfolio portfolio;
    private TransactionHistory transactionHistory;

    // TODO: difficulty level?
    // TODO: password need to be encrypted?

    public User(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}
