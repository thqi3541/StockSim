package use_case.registration;

import entity.Portfolio;
import entity.TransactionHistory;
import entity.User;
import utility.ServiceManager;
import utility.exceptions.DocumentParsingException;
import utility.validations.PasswordValidator;
import utility.validations.UsernameValidator;

/**
 * The Registration Interactor.
 */
public class RegistrationInteractor implements RegistrationInputBoundary {

  // Default initial balance for new users
  private static final double INITIAL_BALANCE = 200000.0;
  // Reference to the output boundary (presenter) for displaying feedback
  private final RegistrationOutputBoundary presenter;
  // Reference to the data access object as an interface type
  private final RegistrationDataAccessInterface dataAccess;

  /**
   * Constructs a RegistrationInteractor with dependencies on presenter, data access, and user
   * factory.
   *
   * @param presenter  The output boundary for displaying results to the user.
   * @param dataAccess The data access object for storing and retrieving users.
   */
  public RegistrationInteractor(RegistrationOutputBoundary presenter,
      RegistrationDataAccessInterface dataAccess) {
    this.presenter = presenter;
    this.dataAccess = dataAccess;

    // Register the interactor with ServiceManager
    ServiceManager.Instance().registerService(RegistrationInputBoundary.class, this);
  }

  /**
   * Processes the registration input data to register a new user. Checks if the username and
   * password are valid and unique. If registration is successful, it creates and saves a new user
   * and sends success feedback to the presenter. Otherwise, it sends an error message to the
   * presenter.
   *
   * @param inputData The input data containing the username and password for registration.
   */
  @Override
  public void execute(RegistrationInputData inputData) {
    try {
      // Check for empty username/password, matching passwords, password strength, and valid username
      validateInput(inputData);

      // Check if username exists
      if (!dataAccess.hasUsername(inputData.username())) {
        // Create a new user
        User newUser = new User(inputData.username(), inputData.password(), INITIAL_BALANCE,
            new Portfolio(),
            new TransactionHistory());

                // Save the user
                dataAccess.createUser(newUser);

        // Notify success
        presenter.prepareSuccessView(
            new RegistrationOutputData("Registration successful! Please log in."));
      } else {
        throw new DuplicateUsernameException("Username already exists!");
      }


    } catch (InvalidInputException e) {
      presenter.prepareInvalidInputView(e.getMessage());
    } catch (DuplicateUsernameException e) {
      presenter.prepareDuplicateUsernameView(e.getMessage());
    } catch (PasswordsDoNotMatchException e) {
      presenter.preparePasswordsDoNotMatchView(e.getMessage());
    } catch (WeakPasswordException e) {
      presenter.prepareWeakPasswordView(e.getMessage());
    } catch (InvalidUsernameException e) {
      presenter.prepareInvalidUsernameView(e.getMessage());
    } catch (DocumentParsingException e) {
      throw new RuntimeException(e);
    }
  }

  private void validateInput(
      RegistrationInputData inputData)
      throws InvalidInputException, PasswordsDoNotMatchException, WeakPasswordException, InvalidUsernameException {
    if (inputData.username().isEmpty() || inputData.password().isEmpty()) {
      throw new InvalidInputException("Username and password cannot be empty.");
    } else if (!inputData.password().equals(inputData.confirmPassword())) {
      throw new PasswordsDoNotMatchException("Passwords do not match.");
    }

    // Use PasswordValidator here
    else if (!PasswordValidator.isStrongPassword(inputData.password())) {
      throw new WeakPasswordException(
          "Password must be at least 8 characters long and include a mix of uppercase, lowercase, numbers, and special characters.");
    }

    // Check if username is valid by calling UsernameValidator
    String usernameValidationMessage = UsernameValidator.validateUsername(inputData.username());
    if (!usernameValidationMessage.isEmpty()) {
      throw new InvalidUsernameException(usernameValidationMessage);
    }
  }

  static class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
      super(message);
    }
  }

  static class InvalidUsernameException extends Exception {

    public InvalidUsernameException(String message) {
      super(message);
    }
  }

  static class PasswordsDoNotMatchException extends Exception {

    public PasswordsDoNotMatchException(String message) {
      super(message);
    }
  }

  static class WeakPasswordException extends Exception {

    public WeakPasswordException(String message) {
      super(message);
    }
  }

  static class DuplicateUsernameException extends Exception {

    public DuplicateUsernameException(String message) {
      super(message);
    }
  }
}

