package use_case.registration;

import data_access.InMemoryUserDataAccessObject;
import entity.User;
import entity.UserFactory;

public class RegistrationInteractor implements RegistrationInputBoundary {
    private final RegistrationOutputBoundary presenter;
    private final InMemoryUserDataAccessObject dataAccess;
    private final UserFactory userFactory;

    public RegistrationInteractor(RegistrationOutputBoundary presenter, InMemoryUserDataAccessObject dataAccess, UserFactory userFactory) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.userFactory = userFactory;
    }

    @Override
    public void register(RegistrationInputData inputData) {
        String username = inputData.username();
        String password = inputData.password();

        if (username.isEmpty() || password.isEmpty()) {
            presenter.prepareFailView("Username and password cannot be empty.");
        }
        else if (dataAccess.getUserWithUsername(username) != null) {
            presenter.prepareFailView("Username already exists. Please choose another one.");
        }
        else {
            User newUser = userFactory.create(username, password);
            dataAccess.saveUser(newUser);
            presenter.prepareSuccessView(new RegistrationOutputData("Registration successful! Please log in."));
        }
    }
}

