package data_transfer;

public record UserDTO(
        String username,
        String password,
        double balance,
        PortfolioDTO portfolio,
        TransactionHistoryDTO transactionHistory) {}
