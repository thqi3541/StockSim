package use_case.login;

import entity.Stock;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import utility.ClientSessionManager;
import utility.SessionManager;
import utility.StockMarket;
import utility.exceptions.ValidationException;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;

class LoginInteractorTest {

    private LoginDataAccessInterface dataAccess;
    private LoginOutputBoundary outputPresenter;
    private LoginInteractor interactor;
    private StockMarket stockMarketMock;
    private SessionManager sessionManagerMock;
    private ClientSessionManager clientSessionManagerMock;

    @BeforeEach
    void setUp() {
        dataAccess = mock(LoginDataAccessInterface.class);
        outputPresenter = mock(LoginOutputBoundary.class);
        interactor = new LoginInteractor(dataAccess, outputPresenter);
        stockMarketMock = mock(StockMarket.class);
        sessionManagerMock = mock(SessionManager.class);
        clientSessionManagerMock = mock(ClientSessionManager.class);
    }

    @Test
    void successfulLoginTest() throws ValidationException {
        User mockUser = new User("testUser", "password");
        String credential = "session123";
        Stock mockStock = new Stock("AAPL", "Apple Inc.", "Technology", 150.0);
        Map<String, Stock> stocks = Collections.singletonMap("AAPL", mockStock);

        when(dataAccess.getUserWithPassword("testUser", "password")).thenReturn(mockUser);

        try (MockedStatic<SessionManager> sessionManagerMockedStatic = Mockito.mockStatic(SessionManager.class);
             MockedStatic<ClientSessionManager> clientSessionManagerMockedStatic = Mockito.mockStatic(ClientSessionManager.class);
             MockedStatic<StockMarket> stockMarketMockedStatic = Mockito.mockStatic(StockMarket.class)) {

            sessionManagerMockedStatic.when(SessionManager::Instance).thenReturn(sessionManagerMock);
            clientSessionManagerMockedStatic.when(ClientSessionManager::Instance).thenReturn(clientSessionManagerMock);
            stockMarketMockedStatic.when(StockMarket::Instance).thenReturn(stockMarketMock);

            when(sessionManagerMock.createSession("testUser")).thenReturn(credential);
            when(stockMarketMock.getStocks()).thenReturn(stocks);

            LoginInputData inputData = new LoginInputData("testUser", "password");
            interactor.execute(inputData);

            verify(outputPresenter).prepareSuccessView(new LoginOutputData(mockUser));
            verify(clientSessionManagerMock).setCredential(credential);
            verify(stockMarketMock).getStocks();
        }
    }

    @Test
    void validationErrorTest() throws ValidationException {
        when(dataAccess.getUserWithPassword("invalidUser", "wrongPassword"))
                .thenThrow(new ValidationException());

        LoginInputData inputData = new LoginInputData("invalidUser", "wrongPassword");
        interactor.execute(inputData);

        verify(outputPresenter).prepareValidationExceptionView();
        verifyNoInteractions(stockMarketMock);
        verifyNoInteractions(sessionManagerMock);
        verifyNoInteractions(clientSessionManagerMock);
    }
}