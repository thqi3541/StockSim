package use_case.registration;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.exceptions.DuplicateUsernameException;
import utility.exceptions.InvalidInputException;
import utility.exceptions.PasswordsDoNotMatchException;

import static org.mockito.Mockito.*;

class RegistrationInteractorTest {

    private RegistrationInteractor interactor;
    private RegistrationOutputBoundary mockPresenter;
    private RegistrationDataAccessInterface mockDataAccess;
    private UserFactory mockUserFactory;

    @BeforeEach
    void setUp() {
        // Create mock objects
        mockPresenter = mock(RegistrationOutputBoundary.class);
        mockDataAccess = mock(RegistrationDataAccessInterface.class);
        mockUserFactory = mock(UserFactory.class);

        // Initialize RegistrationInteractor with mocks
        interactor = new RegistrationInteractor(mockPresenter, mockDataAccess, mockUserFactory);
    }

    @Test
    void testRegisterWithEmptyUsername() {
        // Arrange
        RegistrationInputData inputData = new RegistrationInputData("", "password123", "password123");

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockPresenter).prepareInvalidInputView("Username and password cannot be empty.");
    }

    @Test
    void testRegisterWithEmptyPassword() {
        // Arrange
        RegistrationInputData inputData = new RegistrationInputData("newuser", "", "");

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockPresenter).prepareInvalidInputView("Username and password cannot be empty.");
    }

    @Test
    void testRegisterWithNonMatchingPasswords() {
        // Arrange
        RegistrationInputData inputData = new RegistrationInputData("newuser", "password123", "differentPassword");

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockPresenter).preparePasswordsDoNotMatchView("Passwords do not match.");
    }

    @Test
    void testRegisterWithExistingUsername() throws DuplicateUsernameException {
        String username = "existingUser";
        String password = "password123";

        // Simulate an existing user in the mock data access object
        User existingUser = new User(username, password);
        mockDataAccess.saveUser(existingUser);

        RegistrationInputData inputData = new RegistrationInputData(username, password, password);

        interactor.execute(inputData);

        // Verify that the failure view was prepared with an appropriate message
        verify(mockPresenter).prepareDuplicateUsernameView("Username already exists. Please choose another one.");
    }

    @Test
    void testRegisterWithValidNewUser() throws DuplicateUsernameException {
        // Arrange
        String username = "uniqueUser";
        String password = "password123";
        RegistrationInputData inputData = new RegistrationInputData(username, password, password);

        // Simulate user creation
        User newUser = new User(username, password);
        when(mockUserFactory.create(username, password)).thenReturn(newUser);

        // Act
        interactor.execute(inputData);

        // Assert
        // Verify that a new user was saved
        verify(mockDataAccess).saveUser(newUser);

        // Verify that the success view was prepared with the success message
        verify(mockPresenter).prepareSuccessView(new RegistrationOutputData("Registration successful! Please log in."));
    }
}