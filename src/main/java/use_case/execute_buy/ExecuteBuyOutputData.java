package use_case.execute_buy;

import entity.Portfolio;
import entity.TransactionHistory;

/**
 * This class represents the output data for the ExecuteBuy use case.
 *
 * @param newBalance   the new balance of the user
 * @param newPortfolio the new portfolio of the user
 */
public record ExecuteBuyOutputData(
        double newBalance, Portfolio newPortfolio, TransactionHistory newTransactionHistory) {}
