package entity;

/**
 * A class representing a user
 */
public class User {
    private final String username;
    private final String password;
    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;
    private double balance;

    // TODO: user password should be hashed or encrypted
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.portfolio = new Portfolio();
        this.transactionHistory = new TransactionHistory();
    }

    public User(String username, String password, double balance, Portfolio portfolio, TransactionHistory transactionHistory) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
    }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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
