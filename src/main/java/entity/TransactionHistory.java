package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** A class representing the transaction history of a user. */
public class TransactionHistory {

    private final List<Transaction> transactions;

    /** Default constructor for TransactionHistory class. Initializes an empty transaction history. */
    public TransactionHistory() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Constructor for TransactionHistory class. Takes in existing transactions when there are already some.
     *
     * @param transactions the list of existing transactions
     * @throws IllegalArgumentException if the transactions list is null
     */
    public TransactionHistory(List<Transaction> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException("Transaction list cannot be null.");
        }
        this.transactions = new ArrayList<>(transactions);
    }

    /**
     * Adds a new transaction to the transaction history.
     *
     * @param transaction the transaction to add
     * @throws NullPointerException if the transaction is null
     */
    public synchronized void addTransaction(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null.");
        this.transactions.add(transaction);
    }

    /**
     * Retrieves all transactions in the transaction history. Returns an unmodifiable view of the transactions list to
     * ensure immutability.
     *
     * @return an unmodifiable list of all transactions
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public String toString() {
        return "TransactionHistory{" + "transactions=" + transactions + '}';
    }
}
