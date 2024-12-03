package use_case.execute_sell;

import entity.Portfolio;
import entity.TransactionHistory;

/**
 * This class represents the output data for the ExecuteSell use case.
 *
 * @param newBalance the new balance of the user
 * @param newPortfolio the new portfolio of the user
 * @param newTransactionHistory the new transaction history of the user
 */
public record ExecuteSellOutputData(
        double newBalance, Portfolio newPortfolio, TransactionHistory newTransactionHistory) {}
