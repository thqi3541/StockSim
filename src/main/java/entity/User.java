package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * A class representing a user.
 */
public class User {

    private final String username;
    private final String password; // TODO: Implement hashing or encryption
    private final Portfolio portfolio;
    private final TransactionHistory transactionHistory;
    private double balance;

    /**
     * Constructor for a new user with default balance and empty portfolio/transaction history.
     *
     * @param username the username of the user
     * @param password the user's password (should be hashed/encrypted)
     */
    public User(String username, String password) {
        this.username = Objects.requireNonNull(username, "Username cannot be null.");
        this.password = Objects.requireNonNull(password, "Password cannot be null.");
        this.balance = 0.0;
        this.portfolio = new Portfolio();
        this.transactionHistory = new TransactionHistory();
    }

    /**
     * Full constructor for creating a user with all details.
     *
     * @param username           the username of the user
     * @param password           the user's password
     * @param balance            the user's initial balance
     * @param portfolio          the user's portfolio
     * @param transactionHistory the user's transaction history
     */
    @JsonCreator
    public User(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("balance") double balance,
            @JsonProperty("portfolio") Portfolio portfolio,
            @JsonProperty("transactionHistory") TransactionHistory transactionHistory) {
        this.username = username;
        this.password = password;
        this.balance = balance;
        this.portfolio = portfolio;
        this.transactionHistory = transactionHistory;
    }

    /**
     * Adds an amount to the user's balance.
     *
     * @param amount the amount to add (must be positive)
     * @throws IllegalArgumentException if the amount is negative
     */
    public void addBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to add cannot be negative.");
        }
        this.balance += amount;
    }

    /**
     * Deducts an amount from the user's balance.
     *
     * @param amount the amount to deduct (must be positive)
     * @throws IllegalArgumentException if the amount is negative or exceeds the current balance
     */
    public void deductBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount to deduct cannot be negative.");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Insufficient balance.");
        }
        this.balance -= amount;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password; // WARNING: Only use hashed passwords in production
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                ", portfolio=" + portfolio +
                ", transactionHistory=" + transactionHistory +
                '}';
    }
}
