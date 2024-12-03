package use_case.execute_buy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import entity.Stock;
import entity.User;
import entity.UserStock;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utility.MarketTracker;
import utility.SessionManager;
import utility.exceptions.ValidationException;
import view.ViewManager;

class ExecuteBuyInteractorTest {

    private ExecuteBuyDataAccessInterface dataAccess;
    private ExecuteBuyOutputBoundary outputPresenter;
    private MarketTracker marketTrackerMock;
    private ViewManager viewManagerMock;
    private SessionManager sessionManagerMock;

    @BeforeEach
    void setUp() {
        dataAccess = mock(ExecuteBuyDataAccessInterface.class);
        outputPresenter = mock(ExecuteBuyOutputBoundary.class);
        marketTrackerMock = mock(MarketTracker.class);
    }

    @Test
    void successTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", 10);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify success view was prepared with updated data
            verify(outputPresenter).prepareSuccessView(any(ExecuteBuyOutputData.class));

            // Verify portfolio was updated
            Optional<UserStock> userStockOpt = mockUser.getPortfolio().getUserStock("AAPL");
            assertTrue(userStockOpt.isPresent(), "Portfolio should contain the ticker AAPL");
            assertEquals(10, userStockOpt.get().getQuantity(), "Stock quantity should match");

            // Verify balance was deducted
            assertEquals(8500.0, mockUser.getBalance(), "Balance should be reduced by total cost");
        }
    }

    @Test
    void negativeQuantityTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", -10);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify error view was prepared
            verify(outputPresenter).prepareInvalidQuantityExceptionView();

            // Verify no changes were made
            assertFalse(
                    mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
                    "Stock should not be in portfolio due to negative quantity");
            assertEquals(10000.0, mockUser.getBalance(), "Balance should remain unchanged");
            assertTrue(
                    mockUser.getTransactionHistory().getTransactions().isEmpty(), "No transaction should be recorded");
        }
    }

    @Test
    void insufficientBalanceTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(500.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", 10);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify error view was prepared
            verify(outputPresenter).prepareInsufficientBalanceExceptionView();

            // Verify no changes were made
            assertFalse(
                    mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
                    "Stock should not be in portfolio due to insufficient funds");
            assertEquals(500.0, mockUser.getBalance(), "Balance should remain unchanged");
            assertTrue(
                    mockUser.getTransactionHistory().getTransactions().isEmpty(), "No transaction should be recorded");
        }
    }

    @Test
    void stockNotFoundTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.empty());

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", 10);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify error view was prepared
            verify(outputPresenter).prepareStockNotFoundExceptionView();

            // Verify no changes were made
            assertFalse(
                    mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
                    "Stock should not be in portfolio due to stock not found");
            assertEquals(10000.0, mockUser.getBalance(), "Balance should remain unchanged");
            assertTrue(
                    mockUser.getTransactionHistory().getTransactions().isEmpty(), "No transaction should be recorded");
        }
    }

    //    // TODO: Fail the test
    //    @Test
    //    void validationExceptionTest() throws ValidationException {
    //        User mockUser = createMockUserWithBalance(10000.0);
    //
    //        ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", -10);
    //        ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);
    //
    //        interactor.execute(inputData);
    //
    //        // Verify error view was prepared
    //        verify(outputPresenter).prepareValidationExceptionView();
    //
    //        // Verify no changes were made
    //        assertFalse(mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
    //                "Stock should not be in portfolio due to validation exception");
    //        assertEquals(10000.0, mockUser.getBalance(), "Balance should remain unchanged");
    //        assertTrue(mockUser.getTransactionHistory().getAllTransactions().isEmpty(),
    //                "No transaction should be recorded");
    //    }

    private User createMockUserWithBalance(double balance) throws ValidationException {
        User user = new User("testUser", "password");
        user.addBalance(balance);
        when(dataAccess.getUserWithCredential("dummy")).thenReturn(user);
        return user;
    }
}
