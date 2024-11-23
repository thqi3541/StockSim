package use_case.execute_buy;

import entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utility.exceptions.ValidationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExecuteBuyInteractorTest {

    private ExecuteBuyDataAccessInterface dataAccess;
    private ExecuteBuyOutputBoundary outputPresenter;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new UserFactory();
        dataAccess = mock(ExecuteBuyDataAccessInterface.class);
        outputPresenter = mock(ExecuteBuyOutputBoundary.class);
    }

    @Test
    void successTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(10000.0);
        Stock stock = new Stock("XXXX", "X Company", "Technology", 100.0);

        try (MockedStatic<StockMarket> mockedStatic = Mockito.mockStatic(StockMarket.class)) {
            StockMarket stockMarketMock = Mockito.mock(StockMarket.class);
            mockedStatic.when(StockMarket::Instance).thenReturn(stockMarketMock);
            when(stockMarketMock.getStock("XXXX")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "XXXX", 100);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            // Verify the success view was prepared correctly
            verify(outputPresenter).prepareSuccessView(any());
            Optional<UserStock> userStockOpt = mockUser.getPortfolio().getUserStock("XXXX");
            assertTrue(userStockOpt.isPresent(), "Portfolio should contain the ticker " + "XXXX");
            assertEquals(100, userStockOpt.get().getQuantity(), "Stock quantity should match.");
        }
    }

    @Test
    void insufficientBalanceTest() throws ValidationException {
        User mockUser = createMockUserWithBalance(500.0);
        Stock stock = new Stock("XXXX", "X Company", "Technology", 100.0);

        try (MockedStatic<StockMarket> mockedStatic = Mockito.mockStatic(StockMarket.class)) {
            StockMarket stockMarketMock = Mockito.mock(StockMarket.class);
            mockedStatic.when(StockMarket::Instance).thenReturn(stockMarketMock);
            when(stockMarketMock.getStock("XXXX")).thenReturn(Optional.of(stock));

            ExecuteBuyInputData inputData = new ExecuteBuyInputData("dummy", "XXXX", 100);
            ExecuteBuyInteractor interactor = new ExecuteBuyInteractor(dataAccess, outputPresenter);

            interactor.execute(inputData);

            verify(outputPresenter).prepareInsufficientBalanceExceptionView();
            assertFalse(mockUser.getPortfolio().getUserStock("XXXX").isPresent(),
                    "Stock should not be in portfolio due to insufficient funds.");
        }
    }

    private User createMockUserWithBalance(double balance) throws ValidationException {
        User user = userFactory.create("testUser", "password");
        user.addBalance(balance);
        when(dataAccess.getUserWithCredential("dummy")).thenReturn(user);
        return user;
    }
}
