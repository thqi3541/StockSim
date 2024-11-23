package use_case.login;

import entity.User;
import utility.ClientSessionManager;
import utility.ServiceManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginDataAccessInterface dataAccess;
    private final LoginOutputBoundary outputPresenter;
    private final Executor executor;

    public LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary outputBoundary) {
        this(dataAccess, outputBoundary, Executors.newSingleThreadExecutor());
        ServiceManager.Instance().registerService(LoginInputBoundary.class, this);
    }

    // Constructor for testing - allows injecting a synchronous executor
    LoginInteractor(LoginDataAccessInterface dataAccess, LoginOutputBoundary outputBoundary, Executor executor) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
        this.executor = executor;
        ServiceManager.Instance().registerService(LoginInputBoundary.class, this);
    }

    @Override
    public CompletableFuture<Void> execute(LoginInputData data) {
        return CompletableFuture.runAsync(() -> {
            try {
                User currentUser = dataAccess.getUserWithPassword(data.username(), data.password());
                String credential = SessionManager.Instance().createSession(data.username());
                ClientSessionManager.Instance().setCredential(credential);

                outputPresenter.prepareSuccessView(new LoginOutputData(currentUser));

            } catch (ValidationException e) {
                outputPresenter.prepareValidationExceptionView();
            }
        }, executor);
    }
}
