package view.view_events;

import entity.TransactionHistory;

import java.util.EnumSet;

/**
 * This class handles the ViewEvent for the ViewHistory use case
 */
public class UpdateTransactionHistoryEvent extends ViewEvent {
    private final TransactionHistory transactionHistory;

    /**
     * Constructs a UpdateTransactionHistoryEvent with a specified transaction history,
     * using only the VIEW_HISTORY event type
     *
     * @param transactionHistory The transaction history to display
     */
    public UpdateTransactionHistoryEvent(TransactionHistory transactionHistory) {
        super(EnumSet.of(EventType.UPDATE_TRANSACTION_HISTORY));
        this.transactionHistory = transactionHistory;
    }

    /**
     * A getter for the transaction history associated with this view event
     *
     * @return The transaction history for this view event's user
     */
    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}
