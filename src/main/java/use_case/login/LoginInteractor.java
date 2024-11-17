package use_case.login;

import entity.User;
import utility.ClientSessionManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;

import java.util.concurrent.CompletableFuture;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginDataAccessInterface dataAccess;
    private final LoginOutputBoundary outputPresenter;

    public LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    @Override
    public void execute(LoginInputData data) {
        CompletableFuture.runAsync(() -> {
            try {
                User currentUser = dataAccess.getUserWithPassword(data.username(), data.password());
                String credential = SessionManager.Instance().createSession(data.username());
                ClientSessionManager.Instance().setCredential(credential);

                // Don't fetch stock data here, let the StockMarket handle it
                outputPresenter.prepareSuccessView(new LoginOutputData(currentUser));

            } catch (ValidationException e) {
                outputPresenter.prepareValidationExceptionView();
            } catch (Exception e) {
                outputPresenter.prepareFailView("An error occurred during login: " + e.getMessage());
            }
        });
    }
}
