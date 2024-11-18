package use_case.registration;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utility.exceptions.DuplicateUsernameException;
import utility.exceptions.InvalidInputException;
import utility.exceptions.PasswordsDoNotMatchException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test for RegistrationInteractor
 */
class RegistrationInteractorTest {

    private RegistrationOutputBoundary outputPresenter;
    private RegistrationDataAccessInterface dataAccess;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        // Create mocks
        outputPresenter = Mockito.mock(RegistrationOutputBoundary.class);
        dataAccess = Mockito.mock(RegistrationDataAccessInterface.class);
        userFactory = new UserFactory();
    }

    @Test
    void successTest() throws DuplicateUsernameException {
        // Prepare input data
        String username = "uniqueUser";
        String password = "password123";
        String confirmPassword = "password123";
        RegistrationInputData inputData = new RegistrationInputData(username, password, confirmPassword);

        // Create user instance
        User newUser = userFactory.create(username, password);

        // Mock data access to not throw any exception when saving the user
        doNothing().when(dataAccess).saveUser(newUser);

        // Create interactor
        RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess, userFactory);
        interactor.execute(inputData);

        // Verify that success view is prepared
        verify(outputPresenter).prepareSuccessView(new RegistrationOutputData("Registration successful! Please log in."));
    }

    @Test
    void emptyUsername() {
        // Prepare input data with empty username
        RegistrationInputData inputDataEmptyUsername = new RegistrationInputData("", "password123", "password123");

        // Create interactor
        RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess, userFactory);
        interactor.execute(inputDataEmptyUsername);

        // Verify that the invalid input view is prepared for empty username
        verify(outputPresenter).prepareInvalidInputView("Username and password cannot be empty.");

    }

    @Test
    void emptyPasswordTest() {
        // Prepare input data with empty password
        RegistrationInputData inputDataEmptyPassword = new RegistrationInputData("newuser", "", "");

        // Create interactor
        RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess, userFactory);
        interactor.execute(inputDataEmptyPassword);

        // Verify that the invalid input view is prepared for empty password
        verify(outputPresenter).prepareInvalidInputView("Username and password cannot be empty.");
    }

    @Test
    void passwordsDoNotMatchTest() {
        // Prepare input data with mismatched passwords
        RegistrationInputData inputData = new RegistrationInputData("newuser", "password123", "password456");

        // Create interactor
        RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess, userFactory);
        interactor.execute(inputData);

        // Verify that the passwords do not match view is prepared
        verify(outputPresenter).preparePasswordsDoNotMatchView("Passwords do not match.");
    }

    @Test
    void existingUsernameTest() throws DuplicateUsernameException {
        // Prepare input data with an existing username
        String existingUsername = "existingUser";
        String password = "password123";
        String confirmPassword = "password123";
        RegistrationInputData inputData = new RegistrationInputData(existingUsername, password, confirmPassword);

        // Mock data access to indicate that the user already exists
        doThrow(new DuplicateUsernameException("Username already exists. Please choose another one."))
                .when(dataAccess).saveUser(any(User.class));

        // Create interactor
        RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess, userFactory);
        interactor.execute(inputData);

        // Verify that the duplicate username view is prepared
        verify(outputPresenter).prepareDuplicateUsernameView("Username already exists. Please choose another one.");
    }
}