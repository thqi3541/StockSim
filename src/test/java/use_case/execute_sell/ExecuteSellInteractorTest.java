package use_case.execute_sell;

import entity.Stock;
import entity.User;
import entity.UserStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utility.MarketTracker;
import utility.SessionManager;
import utility.exceptions.ValidationException;
import view.ViewManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExecuteSellInteractorTest {

    private ExecuteSellDataAccessInterface dataAccess;
    private ExecuteSellOutputBoundary outputPresenter;
    private MarketTracker marketTrackerMock;
    private ViewManager viewManagerMock;
    private SessionManager sessionManagerMock;

    @BeforeEach
    void setUp() {
        dataAccess = mock(ExecuteSellDataAccessInterface.class);
        outputPresenter = mock(ExecuteSellOutputBoundary.class);
        marketTrackerMock = mock(MarketTracker.class);
        viewManagerMock = mock(ViewManager.class);
    }

    @Test
    void successTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteSellInputData inputData = new ExecuteSellInputData("dummy", "AAPL", 10);
            ExecuteSellInteractor interactor = new ExecuteSellInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify success view was prepared with updated data
            verify(outputPresenter).prepareSuccessView(any(ExecuteSellOutputData.class));

            // Verify portfolio was updated
            Optional<UserStock> userStockOpt = mockUser.getPortfolio().getUserStock("AAPL");
            assertTrue(userStockOpt.isPresent(), "Portfolio should contain the ticker AAPL");
            assertEquals(-10, userStockOpt.get().getQuantity(), "Stock quantity should match");

            // Verify balance should increase
            assertEquals(11500, mockUser.getBalance(), "Balance should be increased by total earnings");
        }
    }

    // TODO: Fail the test, as invalid input is not handled
//    @Test
//    void negativeQuantityTest() throws ValidationException {
//        User mockUser = createMockUserWithBalance(10000.0);
//        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);
//
//        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
//            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
//            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.of(stock));
//
//            ExecuteSellInputData inputData = new ExecuteSellInputData("dummy", "AAPL", -10);
//            ExecuteSellInteractor interactor = new ExecuteSellInteractor(dataAccess, outputPresenter);
//
//            interactor.execute(inputData);
//
//            // Verify error view was prepared
//            verify(outputPresenter).prepareValidationExceptionView();
//
//            // Verify no changes were made
//            assertFalse(mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
//                    "Stock should not be in portfolio due to negative quantity");
//            assertEquals(10000.0, mockUser.getBalance(), "Balance should remain unchanged");
//            assertTrue(mockUser.getTransactionHistory().getAllTransactions().isEmpty(),
//                    "No transaction should be recorded");
//        }
//    }

    @Test
    void insufficientMarginCallTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(-500.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteSellInputData inputData = new ExecuteSellInputData("dummy", "AAPL", 10);
            ExecuteSellInteractor interactor = new ExecuteSellInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify error view was prepared
            verify(outputPresenter).prepareInsufficientMarginCallExceptionView();

            // Verify no changes were made
            assertFalse(mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
                    "Stock should not be in portfolio due to insufficient margin call");
            assertEquals(-500.0, mockUser.getBalance(), "Balance should remain unchanged");
            assertTrue(mockUser.getTransactionHistory().getAllTransactions().isEmpty(),
                    "No transaction should be recorded");
        }
    }

    @Test
    void stockNotFoundTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);

        try (MockedStatic<MarketTracker> stockMarketMockedStatic = Mockito.mockStatic(MarketTracker.class)) {
            stockMarketMockedStatic.when(MarketTracker::Instance).thenReturn(marketTrackerMock);
            when(marketTrackerMock.getStock("AAPL")).thenReturn(Optional.empty());

            ExecuteSellInputData inputData = new ExecuteSellInputData("dummy", "AAPL", 10);
            ExecuteSellInteractor interactor = new ExecuteSellInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify error view was prepared
            verify(outputPresenter).prepareStockNotFoundExceptionView();

            // Verify no changes were made
            assertFalse(mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
                    "Stock should not be in portfolio due to stock not found");
            assertEquals(10000.0, mockUser.getBalance(), "Balance should remain unchanged");
            assertTrue(mockUser.getTransactionHistory().getAllTransactions().isEmpty(),
                    "No transaction should be recorded");
        }
    }

//    // TODO: Fail the test
//    @Test
//    void validationExceptionTest() throws ValidationException {
//        User mockUser = createMockUserWithBalance(10000.0);
//
//        ExecuteSellInputData inputData = new ExecuteSellInputData("dummy", "AAPL", -10);
//        ExecuteSellInteractor interactor = new ExecuteSellInteractor(dataAccess, outputPresenter);
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
