package use_case.login;

import entity.User;
import utility.exceptions.ValidationException;

/**
 * The interface of the DAO for the Login Use Case.
 */
public interface LoginDataAccessInterface {

    /**
     * Retrieves the user associated with the given username and password.
     *
     * @param username the user's username
     * @param password the user's password
     * @return the user associated with the provided credentials
     * @throws ValidationException if the username or password is invalid
     */
    User getUserWithPassword(String username, String password) throws ValidationException;
}
