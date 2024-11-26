package use_case.view_history;

import entity.TransactionHistory;

/**
 * This class represents the output data for the ViewHistory use case.
 *
 * @param transactionHistory the transaction history of the user
 */
public record ViewHistoryOutputData(TransactionHistory transactionHistory) {
}
