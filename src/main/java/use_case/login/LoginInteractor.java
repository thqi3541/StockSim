package use_case.login;

import entity.StockMarket;
import entity.User;
import utility.ClientSessionManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginDataAccessInterface dataAccess;
    private final LoginOutputBoundary outputPresenter;

    public LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    @Override
    public void execute(LoginInputData data) {
        try {
            User currentUser = dataAccess.getUserWithPassword(data.username(), data.password());
            String credential = SessionManager.Instance().createSession(data.username());
            // TODO: when we go to multi-client app, passing credential to client through output data
            ClientSessionManager.Instance().setCredential(credential);
            outputPresenter.prepareSuccessView(new LoginOutputData(
                    currentUser,
                    StockMarket.Instance().getStocks()
            ));
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        }
    }
}
