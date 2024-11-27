package data_transfer.mapper;

import data_transfer.PortfolioDTO;
import data_transfer.StockDTO;
import data_transfer.TransactionDTO;
import data_transfer.TransactionHistoryDTO;
import data_transfer.UserDTO;
import data_transfer.UserStockDTO;
import entity.Portfolio;
import entity.Stock;
import entity.Transaction;
import entity.TransactionHistory;
import entity.User;
import entity.UserStock;

import java.util.Map;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getUsername(),
                user.getPassword(),
                user.getBalance(),
                toDTO(user.getPortfolio()),
                toDTO(user.getTransactionHistory())
        );
    }

    public static PortfolioDTO toDTO(Portfolio portfolio) {
        return new PortfolioDTO(portfolio.getUserStocks()
                                         .entrySet()
                                         .stream()
                                         .collect(Collectors.toMap(
                                                 Map.Entry::getKey,
                                                 entry -> toDTO(
                                                         entry.getValue())
                                         ))
        );
    }

    public static UserStockDTO toDTO(UserStock userStock) {
        return new UserStockDTO(
                toDTO(userStock.getStock()),
                userStock.getAvgCost(),
                userStock.getQuantity()
        );
    }

    public static StockDTO toDTO(Stock stock) {
        return new StockDTO(
                stock.getTicker(),
                stock.getCompany(),
                stock.getIndustry(),
                stock.getMarketPrice()
        );
    }

    public static TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.timestamp(),
                transaction.ticker(),
                transaction.quantity(),
                transaction.executionPrice(),
                transaction.type()
        );
    }

    public static TransactionHistoryDTO toDTO(
            TransactionHistory transactionHistory) {
        return new TransactionHistoryDTO(transactionHistory.getTransactions()
                                                           .stream()
                                                           .map(UserMapper::toDTO)
                                                           .toList()
        );
    }

    public static User fromDTO(UserDTO userDTO) {
        return new User(
                userDTO.username(),
                userDTO.password(),
                userDTO.balance(),
                fromDTO(userDTO.portfolio()),
                fromDTO(userDTO.transactionHistory())
        );
    }

    public static Portfolio fromDTO(PortfolioDTO portfolioDTO) {
        return new Portfolio(portfolioDTO.userStocks()
                                         .entrySet()
                                         .stream()
                                         .collect(Collectors.toMap(
                                                 Map.Entry::getKey,
                                                 entry -> fromDTO(
                                                         entry.getValue())
                                         ))
        );
    }

    public static UserStock fromDTO(UserStockDTO userStockDTO) {
        return new UserStock(
                fromDTO(userStockDTO.stock()),
                userStockDTO.avgCost(),
                userStockDTO.quantity()
        );
    }

    public static Stock fromDTO(StockDTO stockDTO) {
        return new Stock(
                stockDTO.ticker(),
                stockDTO.company(),
                stockDTO.industry(),
                stockDTO.marketPrice()
        );
    }

    public static Transaction fromDTO(TransactionDTO transactionDTO) {
        return new Transaction(
                transactionDTO.timestamp(),
                transactionDTO.ticker(),
                transactionDTO.quantity(),
                transactionDTO.executionPrice(),
                transactionDTO.type()
        );
    }

    public static TransactionHistory fromDTO(
            TransactionHistoryDTO transactionHistoryDTO) {
        return new TransactionHistory(transactionHistoryDTO.transactions()
                                                           .stream()
                                                           .map(UserMapper::fromDTO)
                                                           .toList()
        );
    }
}
