package use_case.execute_buy;

import entity.Stock;
import entity.User;
import entity.UserFactory;
import entity.UserStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utility.StockMarket;
import utility.ViewManager;
import utility.exceptions.ValidationException;
import view.view_events.UpdateTransactionHistoryEvent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExecuteBuyInteractorTest {

    private ExecuteBuyDataAccessInterface dataAccess;
    private ExecuteBuyOutputBoundary outputPresenter;
    private UserFactory userFactory;
    private StockMarket stockMarketMock;
    private ViewManager viewManagerMock;

    @BeforeEach
    void setUp() {
        userFactory = new UserFactory();
        dataAccess = mock(ExecuteBuyDataAccessInterface.class);
        outputPresenter = mock(ExecuteBuyOutputBoundary.class);
        stockMarketMock = mock(StockMarket.class);
        viewManagerMock = mock(ViewManager.class);
    }

    @Test
    void successTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<StockMarket> stockMarketMockedStatic = Mockito.mockStatic(StockMarket.class);
             MockedStatic<ViewManager> viewManagerMockedStatic = Mockito.mockStatic(ViewManager.class)) {

            stockMarketMockedStatic.when(StockMarket::Instance).thenReturn(stockMarketMock);
            viewManagerMockedStatic.when(ViewManager::Instance).thenReturn(viewManagerMock);
            when(stockMarketMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", 10);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify success view was prepared
            verify(outputPresenter).prepareSuccessView(any());

            // Verify transaction was added
            Optional<UserStock> userStockOpt = mockUser.getPortfolio().getUserStock("AAPL");
            assertTrue(userStockOpt.isPresent(), "Portfolio should contain the ticker AAPL");
            assertEquals(10, userStockOpt.get().getQuantity(), "Stock quantity should match.");

            // Verify transaction history event was broadcast
            verify(viewManagerMock).broadcastEvent(any(UpdateTransactionHistoryEvent.class));
        }
    }

    @Test
    void insufficientBalanceTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(500.0);
        Stock stock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);

        try (MockedStatic<StockMarket> stockMarketMockedStatic = Mockito.mockStatic(StockMarket.class);
             MockedStatic<ViewManager> viewManagerMockedStatic = Mockito.mockStatic(ViewManager.class)) {

            stockMarketMockedStatic.when(StockMarket::Instance).thenReturn(stockMarketMock);
            viewManagerMockedStatic.when(ViewManager::Instance).thenReturn(viewManagerMock);
            when(stockMarketMock.getStock("AAPL")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "AAPL", 10);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            verify(outputPresenter).prepareInsufficientBalanceExceptionView();
            assertFalse(mockUser.getPortfolio().getUserStock("AAPL").isPresent(),
                    "Stock should not be in portfolio due to insufficient funds.");

            // Verify no transaction history event was broadcast
            verify(viewManagerMock, never()).broadcastEvent(any(UpdateTransactionHistoryEvent.class));
        }
    }

    private User createMockUserWithBalance(double balance) throws ValidationException {
        User user = userFactory.create("testUser", "password");
        user.addBalance(balance);
        when(dataAccess.getUserWithCredential("dummy")).thenReturn(user);
        return user;
    }
}
