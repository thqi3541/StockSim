package use_case.execute_view_history;

import entity.TransactionHistory;

/**
 * This class represents the output data for the ViewHistory use case.
 *
 * @param transactionHistory the transaction history of the user
 */
public record ExecuteViewHistoryOutputData(
        TransactionHistory transactionHistory
) {
}
