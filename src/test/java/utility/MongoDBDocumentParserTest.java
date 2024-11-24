package utility;

import entity.Portfolio;
import entity.Stock;
import entity.User;
import entity.UserStock;
import entity.Transaction;
import entity.TransactionHistory;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import utility.exceptions.DocumentParsingException;

import java.util.Date;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MongoDBDocumentParserTest {

    @Test
    public void testStockSerialization() throws DocumentParsingException {
        // Arrange
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 190.25);

        // Act
        Document stockDoc = MongoDBDocumentParser.toDocument(stock);
        Stock parsedStock = MongoDBDocumentParser.fromDocument(Stock.class, stockDoc);

        // Assert
        assertNotNull(parsedStock);
        assertEquals(stock.getTicker(), parsedStock.getTicker());
        assertEquals(stock.getCompany(), parsedStock.getCompany());
        assertEquals(stock.getIndustry(), parsedStock.getIndustry());
        assertEquals(stock.getMarketPrice(), parsedStock.getMarketPrice());
    }

    @Test
    public void testTransactionSerialization() throws DocumentParsingException {
        // Arrange
        Transaction transaction = new Transaction(new Date(), "GOOG", 100, 2800.50, "BUY");

        // Act
        Document transactionDoc = MongoDBDocumentParser.toDocument(transaction);
        Transaction parsedTransaction = MongoDBDocumentParser.fromDocument(Transaction.class, transactionDoc);

        // Assert
        assertNotNull(parsedTransaction);
        assertEquals(transaction.timestamp(), parsedTransaction.timestamp());
        assertEquals(transaction.ticker(), parsedTransaction.ticker());
        assertEquals(transaction.quantity(), parsedTransaction.quantity());
        assertEquals(transaction.executionPrice(), parsedTransaction.executionPrice());
        assertEquals(transaction.type(), parsedTransaction.type());
    }

    @Test
    public void testUserStockSerialization() throws DocumentParsingException {
        // Arrange
        Stock stock = new Stock("TSLA", "Tesla Inc.", "Automotive", 1200.0);
        UserStock userStock = new UserStock(stock, 1100.0, 5);

        // Act
        Document userStockDoc = MongoDBDocumentParser.toDocument(userStock);
        UserStock parsedUserStock = MongoDBDocumentParser.fromDocument(UserStock.class, userStockDoc);

        // Assert
        assertNotNull(parsedUserStock);
        assertEquals(userStock.getStock().getTicker(), parsedUserStock.getStock().getTicker());
        assertEquals(userStock.getAvgCost(), parsedUserStock.getAvgCost());
        assertEquals(userStock.getQuantity(), parsedUserStock.getQuantity());
    }

    @Test
    public void testPortfolioSerialization() throws DocumentParsingException {
        // Arrange
        UserStock userStock1 = new UserStock(new Stock("META", "Meta Platforms", "Technology", 350.0), 300.0, 10);
        UserStock userStock2 = new UserStock(new Stock("NFLX", "Netflix", "Streaming", 400.0), 380.0, 15);
        Map<String, UserStock> userStocks = Map.of(
                "META", userStock1,
                "NFLX", userStock2
        );
        Portfolio portfolio = new Portfolio(userStocks);

        // Act
        Document portfolioDoc = MongoDBDocumentParser.toDocument(portfolio);
        Portfolio parsedPortfolio = MongoDBDocumentParser.fromDocument(Portfolio.class, portfolioDoc);

        // Assert
        assertNotNull(parsedPortfolio);
        assertEquals(portfolio.getUserStocks().size(), parsedPortfolio.getUserStocks().size());
        for (String key : portfolio.getUserStocks().keySet()) {
            assertEquals(portfolio.getUserStocks().get(key).getStock().getTicker(),
                    parsedPortfolio.getUserStocks().get(key).getStock().getTicker());
        }
    }

    @Test
    public void testTransactionHistorySerialization() throws DocumentParsingException {
        // Arrange
        List<Transaction> transactions = List.of(
                new Transaction(new Date(), "MSFT", 50, 300.0, "SELL"),
                new Transaction(new Date(), "AMZN", 10, 3500.0, "BUY")
        );
        TransactionHistory history = new TransactionHistory(transactions);

        // Act
        Document historyDoc = MongoDBDocumentParser.toDocument(history);
        TransactionHistory parsedHistory = MongoDBDocumentParser.fromDocument(TransactionHistory.class, historyDoc);

        // Assert
        assertNotNull(parsedHistory);
        assertEquals(history.getAllTransactions().size(), parsedHistory.getAllTransactions().size());
        for (int i = 0; i < history.getAllTransactions().size(); i++) {
            assertEquals(history.getAllTransactions().get(i), parsedHistory.getAllTransactions().get(i));
        }
    }

    @Test
    public void testUserSerialization() throws DocumentParsingException {
        // Arrange
        Portfolio portfolio = new Portfolio(Map.of(
                "META", new UserStock(new Stock("META", "Meta Platforms", "Technology", 350.0), 300.0, 10)
        ));
        TransactionHistory transactionHistory = new TransactionHistory(List.of(
                new Transaction(new Date(), "META", 10, 300.0, "BUY")
        ));
        User user = new User("user", "password", 5000.0, portfolio, transactionHistory);

        // Act
        Document userDoc = MongoDBDocumentParser.toDocument(user);
        User parsedUser = MongoDBDocumentParser.fromDocument(User.class, userDoc);

        // Assert
        assertNotNull(parsedUser);
        assertEquals(user.getUsername(), parsedUser.getUsername());
        assertEquals(user.getPassword(), parsedUser.getPassword());
        assertEquals(user.getPortfolio().getUserStocks().size(), parsedUser.getPortfolio().getUserStocks().size());
        assertEquals(user.getBalance(), parsedUser.getBalance());
    }

}
