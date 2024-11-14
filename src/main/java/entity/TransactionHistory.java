package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing the transaction history of a user
 */
public class TransactionHistory {

    private final List<Transaction> transactions;

    /**
     * Constructor for TransactionHistory class
     */
    public TransactionHistory() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Constructor for TransactionHistory class
     */
    public TransactionHistory(List<Transaction> existingTransactions) {
        this.transactions = new ArrayList<>(existingTransactions);
    }

    /**
     * When a transaction is successfully made, add a new transaction to the transaction history.
     * @param transaction the transaction to add
     */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * This is the getter of the TransactionHistory class.
     * @return all transactions
     */
    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Get the most recent transactions by count
     *
     * @param count the number of transactions to get
     * @return the most recent transactions
     */
    public List<Transaction> getRecentTransactions(int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        int start = Math.max(transactions.size() - count, 0);
        return new ArrayList<>(transactions.subList(start, transactions.size()));
    }
}
