package use_case.registration;

import entity.User;
import utility.exceptions.DocumentParsingException;

/** The interface of the DAO for the Registration Use Case. */
public interface RegistrationDataAccessInterface {

    /**
     * Checks if the given username already exists in the database.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean hasUsername(String username);

    /**
     * Creates a new user in the database.
     *
     * @param user the user to be created
     * @throws DocumentParsingException if there is an error parsing the user data
     */
    void createUser(User user) throws DocumentParsingException;
}
