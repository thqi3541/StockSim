package view.view_events;

import entity.TransactionHistory;

import java.util.EnumSet;

/**
 * This class handles the ViewEvent for the ViewHistory use case
 */
public class ViewHistoryEvent extends ViewEvent {
    private final TransactionHistory transactionHistory;

    /**
     * Constructs a ViewHistoryEvent with a specified transaction history,
     * using only the VIEW_HISTORY event type
     *
     * @param transactionHistory The transaction history to display
     */
    public ViewHistoryEvent(TransactionHistory transactionHistory) {
        super(EnumSet.of(EventType.VIEW_HISTORY));
        this.transactionHistory = transactionHistory;
    }

    /**
     * Constructs a ViewHistoryEvent with a specified transaction history and a set of event types.
     *
     * @param transactionHistory The transaction history to display
     * @param types              The set of event types for this event
     */
    public ViewHistoryEvent(TransactionHistory transactionHistory, EnumSet<EventType> types) {
        super(types);
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
