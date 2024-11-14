package use_case.registration;

import data_access.InMemoryRegistrationDataAccessObject;
import entity.User;
import entity.UserFactory;

/**
 * The Registration Interactor.
 */
public class RegistrationInteractor implements RegistrationInputBoundary {

    // Reference to the output boundary (presenter) for displaying feedback
    private final RegistrationOutputBoundary presenter;

    // Reference to the data access object for saving and retrieving user data
    private final InMemoryRegistrationDataAccessObject dataAccess;

    // Reference to the user factory for creating new user instances
    private final UserFactory userFactory;

    /**
     * Constructs a RegistrationInteractor with dependencies on presenter, data access, and user factory.
     *
     * @param presenter   The output boundary for displaying results to the user.
     * @param dataAccess  The data access object for storing and retrieving users.
     * @param userFactory The factory used to create new user instances.
     */
    public RegistrationInteractor(RegistrationOutputBoundary presenter, InMemoryRegistrationDataAccessObject dataAccess, UserFactory userFactory) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
        this.userFactory = userFactory;
    }

    /**
     * Processes the registration input data to register a new user.
     * Checks if the username and password are valid and unique. If registration is successful,
     * it creates and saves a new user and sends success feedback to the presenter.
     * Otherwise, it sends an error message to the presenter.
     *
     * @param inputData The input data containing the username and password for registration.
     */
    @Override
    public void register(RegistrationInputData inputData) {
        String username = inputData.username();
        String password = inputData.password();

        // Check if username or password is empty and show an error message if so
        if (username.isEmpty() || password.isEmpty()) {
            presenter.prepareFailView("Username and password cannot be empty.");
        }

        // Check if the username is already taken by querying the data access object
        else if (dataAccess.getUserWithUsername(username) != null) {
            presenter.prepareFailView("Username already exists. Please choose another one.");
        }

        // Proceed with registration if both checks pass
        else {
            // Create a new user with the provided username and password
            User newUser = userFactory.create(username, password);

            // Save the new user to the data access storage
            dataAccess.saveUser(newUser);

            // Notify the presenter that registration was successful and display a message
            presenter.prepareSuccessView(new RegistrationOutputData("Registration successful! Please log in."));

            // Direct the presenter to switch to the login view
            presenter.switchToLoginView();
        }
    }
}

