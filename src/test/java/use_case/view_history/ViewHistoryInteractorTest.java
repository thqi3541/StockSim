package use_case.view_history;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import entity.Transaction;
import entity.TransactionHistory;
import entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utility.exceptions.ValidationException;

class ViewHistoryInteractorTest {

    private ViewHistoryDataAccessInterface dataAccess;
    private ViewHistoryOutputBoundary outputPresenter;

    @BeforeEach
    void setUp() {
        dataAccess = Mockito.mock(ViewHistoryDataAccessInterface.class);
        outputPresenter = Mockito.mock(ViewHistoryOutputBoundary.class);
    }

    @Test
    void successTest() throws ValidationException {
        User mockUser = new User("testUser", "password");

        when(dataAccess.getUserWithCredential("dummy")).thenReturn(mockUser);

        // prepare input data
        ViewHistoryInputData inputData = new ViewHistoryInputData("dummy");

        // prepare interactor
        ViewHistoryInteractor interactor = new ViewHistoryInteractor(dataAccess, outputPresenter);
        interactor.execute(inputData);

        // check if message is prepared
        verify(outputPresenter).prepareSuccessView(new ViewHistoryOutputData(mockUser.getTransactionHistory()));

        // check if user portfolio is empty
        TransactionHistory mockUserTransactionHistory = mockUser.getTransactionHistory();
        List<Transaction> userTransactions = new ArrayList<>();
        assertEquals(
                userTransactions, mockUserTransactionHistory.getTransactions(), "Transaction History should be empty");

        // mock execute buy transaction
        Date buyTimestamp = new Date(2024, 11, 22, 13, 05, 55);
        String ticker = "XXXX";
        Transaction mockBuyTransaction = new Transaction(buyTimestamp, ticker, 10, 100.0, "buy");
        mockUserTransactionHistory.addTransaction(mockBuyTransaction);

        // check if user history updated buy transaction
        userTransactions.add(mockBuyTransaction);
        assertEquals(
                userTransactions,
                mockUserTransactionHistory.getTransactions(),
                "Transaction History should contain the mockBuyTransaction");

        // mock execute sell transaction
        Date sellTimestamp = new Date(2024, 11, 23, 03, 05, 25);
        Transaction mockSellTransaction = new Transaction(sellTimestamp, ticker, 5, 100.0, "sell");
        mockUserTransactionHistory.addTransaction(mockSellTransaction);

        // check if user history updated sell transaction
        userTransactions.add(mockSellTransaction);
        assertEquals(
                userTransactions,
                mockUserTransactionHistory.getTransactions(),
                "Transaction History should be contain the mockSellTransaction");
    }
}
