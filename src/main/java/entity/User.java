package entity;

public class User {

    private final String username;
    private final String password;
    private final double balance;
    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;

    // TODO: difficulty level?
    // TODO: password need to be encrypted?

    public User(String username, String password, double balance, Portfolio portfolio, TransactionHistory transactionHistory) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
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
