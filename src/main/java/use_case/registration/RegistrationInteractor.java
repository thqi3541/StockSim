package use_case.registration;

import entity.User;
import entity.UserFactory;
import utility.exceptions.DuplicateUsernameException;
import utility.exceptions.InvalidInputException;
import utility.exceptions.PasswordsDoNotMatchException;

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
    public void execute(RegistrationInputData inputData) {
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
        } catch (PasswordsDoNotMatchException e) {
            presenter.preparePasswordsDoNotMatchView(e.getMessage());
        }
    }

    private void validateInput(RegistrationInputData inputData) throws InvalidInputException, DuplicateUsernameException, PasswordsDoNotMatchException{
        if (inputData.username().isEmpty() || inputData.password().isEmpty()) {
            throw new InvalidInputException("Username and password cannot be empty.");
        }

        else if (!inputData.password().equals(inputData.confirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match.");
        }
    }
}

