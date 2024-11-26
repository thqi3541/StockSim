package use_case.registration;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import utility.exceptions.DocumentParsingException;

/**
 * Unit test for RegistrationInteractor.
 */
class RegistrationInteractorTest {

  private RegistrationOutputBoundary outputPresenter;
  private RegistrationDataAccessInterface dataAccess;

  @BeforeEach
  void setUp() {
    // Create mocks
    outputPresenter = Mockito.mock(RegistrationOutputBoundary.class);
    dataAccess = Mockito.mock(RegistrationDataAccessInterface.class);
  }

  @Test
  void successTest() throws Exception {
    // Prepare input data
    String username = "uniqueUser";
    String password = "Password123!";
    String confirmPassword = "Password123!";
    RegistrationInputData inputData = new RegistrationInputData(username, password,
        confirmPassword);

    // Mock data access to not find any existing user with the same username
    doNothing().when(dataAccess).saveUser(any(User.class));

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that success view is prepared
    verify(outputPresenter).prepareSuccessView(
        new RegistrationOutputData("Registration successful! Please log in."));
  }

  @Test
  void emptyUsernameTest() {
    RegistrationInputData inputData = new RegistrationInputData("", "Password123!", "Password123!");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify invalid input view
    verify(outputPresenter).prepareInvalidInputView("Username and password cannot be empty.");
  }

  @Test
  void emptyPasswordTest() {
    RegistrationInputData inputData = new RegistrationInputData("newuser", "", "");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify invalid input view
    verify(outputPresenter).prepareInvalidInputView("Username and password cannot be empty.");
  }

  @Test
  void passwordsDoNotMatchTest() {
    RegistrationInputData inputData = new RegistrationInputData("newuser", "Password123!",
        "DifferentPassword!");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that passwords do not match view is prepared
    verify(outputPresenter).preparePasswordsDoNotMatchView("Passwords do not match.");
  }

  @Test
  void weakPasswordTest() {
    RegistrationInputData inputData = new RegistrationInputData("newuser", "weakpass", "weakpass");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that weak password view is prepared
    verify(outputPresenter).prepareWeakPasswordView(
        "Password must be at least 8 characters long and include a mix of uppercase, lowercase, numbers, and special characters.");
  }

  @Test
  void invalidUsernameShortTest() {
    RegistrationInputData inputData = new RegistrationInputData("usr", "Password123!",
        "Password123!");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that invalid username view is prepared
    verify(outputPresenter).prepareInvalidUsernameView(
        "Username must be at least 5 characters long.");
  }

  @Test
  void invalidUsernameLongTest() {
    RegistrationInputData inputData = new RegistrationInputData(
        "thisisaverylongusernameexceedinglimit",
        "Password123!", "Password123!");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that invalid username view is prepared
    verify(outputPresenter).prepareInvalidUsernameView(
        "Username must be between 5 and 20 characters.");
  }

  @Test
  void invalidUsernameSpecialCharsTest() {
    RegistrationInputData inputData = new RegistrationInputData("user@name", "Password123!",
        "Password123!");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that invalid username view is prepared
    verify(outputPresenter).prepareInvalidUsernameView(
        "Username must start with a letter and contain only letters, digits, or underscores.");
  }

  @Test
  void restrictedUsernameTest() {
    RegistrationInputData inputData = new RegistrationInputData("adminUser", "Password123!",
        "Password123!");

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);

    // Verify that invalid username view is prepared
    verify(outputPresenter).prepareInvalidUsernameView(
        "Username cannot contain restricted words such as 'admin' or 'root'.");
  }

  @Test
  void duplicateUsernameTest() throws DocumentParsingException {
    String existingUsername = "existingUser";
    String password = "Password123!";
    String confirmPassword = "Password123!";
    RegistrationInputData inputData = new RegistrationInputData(existingUsername, password,
        confirmPassword);

    // Mock data access to indicate that the user already exists
    when(dataAccess.hasUsername(existingUsername)).thenReturn(false).thenReturn(true);

    // Create interactor
    RegistrationInteractor interactor = new RegistrationInteractor(outputPresenter, dataAccess);
    interactor.execute(inputData);
    interactor.execute(inputData);

    // Verify that duplicate username view is prepared
    verify(outputPresenter).prepareDuplicateUsernameView("Username already exists!");
    verify(dataAccess, times(1)).saveUser(any(User.class));
  }
}