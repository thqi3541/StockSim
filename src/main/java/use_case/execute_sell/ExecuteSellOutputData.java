package use_case.execute_sell;

import entity.Portfolio;
import entity.TransactionHistory;

public record ExecuteSellOutputData(
        double newBalance, Portfolio newPortfolio, TransactionHistory newTransactionHistory) {}
