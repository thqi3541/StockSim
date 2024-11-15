package view.view_events;

import entity.TransactionHistory;

import java.util.EnumSet;

public class ViewHistoryEvent extends ViewEvent {
    private final TransactionHistory transactionHistory;

    public ViewHistoryEvent(TransactionHistory transactionHistory) {
        super(EnumSet.of(EventType.VIEW_HISTORY));
        this.transactionHistory = transactionHistory;
    }

    public ViewHistoryEvent(TransactionHistory transactionHistory, EnumSet<EventType> types) {
        super(types);
        this.transactionHistory = transactionHistory;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}
