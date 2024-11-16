package use_case.registration;

import entity.User;
import entity.UserFactory;
import utility.exceptions.DuplicateUsernameException;
import utility.exceptions.InvalidInputException;

/**
 * The Registration Interactor.
 */
public class RegistrationInteractor implements RegistrationInputBoundary {

    // Reference to the output boundary (presenter) for displaying feedback
    private final RegistrationOutputBoundary presenter;

    // Reference to the data access object as an interface type
    private final RegistrationDataAccessInterface dataAccess;

    // Reference to the user factory for creating new user instances
    private final UserFactory userFactory;

    /**
     * Constructs a RegistrationInteractor with dependencies on presenter, data access, and user factory.
     *
     * @param presenter   The output boundary for displaying results to the user.
     * @param dataAccess  The data access object for storing and retrieving users.
     * @param userFactory The factory used to create new user instances.
     */
    public RegistrationInteractor(RegistrationOutputBoundary presenter, RegistrationDataAccessInterface dataAccess, UserFactory userFactory) {
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
        try {
            // Check for empty username/password
            validateInput(inputData);

            // Create a new user
            User newUser = userFactory.create(inputData.username(), inputData.password());

            // Save the user
            dataAccess.saveUser(newUser);

            // Notify success
            presenter.prepareSuccessView(new RegistrationOutputData("Registration successful! Please log in."));
        } catch (InvalidInputException e) {
            presenter.prepareInvalidInputView(e.getMessage());
        } catch (DuplicateUsernameException e) {
            presenter.prepareDuplicateUsernameView(e.getMessage());
        }
    }

    private void validateInput(RegistrationInputData inputData) throws InvalidInputException {
        if (inputData.username().isEmpty() || inputData.password().isEmpty()) {
            throw new InvalidInputException("Username and password cannot be empty.");
        }

        // Check if the username already exists
        else if (dataAccess.getUserWithUsername(inputData.username()) != null) {
            throw new DuplicateUsernameException("Username already exists. Please choose another one.");
        }
    }

    /**
     * Exception thrown when the input data is invalid.
     */
    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    /**
     * Exception thrown when the username already exists.
     */
    public static class DuplicateUsernameException extends Exception {
        public DuplicateUsernameException(String message) {
            super(message);
        }
    }
}

