package use_case.registration;

import data_access.InMemoryRegistrationDataAccessObject;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationInteractorTest {

    private RegistrationInteractor interactor;
    private RegistrationOutputBoundary mockPresenter;
    private InMemoryRegistrationDataAccessObject mockDataAccess;
    private UserFactory mockUserFactory;

    @BeforeEach
    void setUp() {
        // Create mock objects
        mockPresenter = mock(RegistrationOutputBoundary.class);
        mockDataAccess = new InMemoryRegistrationDataAccessObject();
        mockUserFactory = mock(UserFactory.class);

        // Initialize RegistrationInteractor with mocks
        interactor = new RegistrationInteractor(mockPresenter, mockDataAccess, mockUserFactory);

    }

    @AfterEach
    void tearDown() {
        // Reset mocks if necessary
    }

    @Test
    void testRegisterWithEmptyUsername() {
        RegistrationInputData inputData = new RegistrationInputData("", "password123");

        interactor.register(inputData);

        // Verify that the failure view was prepared with an appropriate message
        verify(mockPresenter).prepareFailView("Username and password cannot be empty.");
    }

    @Test
    void testRegisterWithEmptyPassword() {
        RegistrationInputData inputData = new RegistrationInputData("newuser", "");

        interactor.register(inputData);

        // Verify that the failure view was prepared with an appropriate message
        verify(mockPresenter).prepareFailView("Username and password cannot be empty.");
    }

    @Test
    void testRegisterWithExistingUsername() {
        String username = "existingUser";
        String password = "password123";

        // Simulate an existing user in the mock data access object
        mockDataAccess.saveUser(new User(username, password));

        RegistrationInputData inputData = new RegistrationInputData(username, password);

        interactor.register(inputData);

        // Verify that the failure view was prepared with an appropriate message
        verify(mockPresenter).prepareFailView("Username already exists. Please choose another one.");
    }

    @Test
    void testRegisterWithValidNewUser() {
        String username = "uniqueUser";
        String password = "password123";

        RegistrationInputData inputData = new RegistrationInputData(username, password);

        // Simulate user creation
        User newUser = new User(username, password);
        when(mockUserFactory.create(username, password)).thenReturn(newUser);

        interactor.register(inputData);

        // Verify that a new user was saved
        assertEquals(newUser, mockDataAccess.getUserWithUsername(username));

        // Verify that the success view was prepared with the success message
        verify(mockPresenter).prepareSuccessView(new RegistrationOutputData("Registration successful! Please log in."));
    }
}