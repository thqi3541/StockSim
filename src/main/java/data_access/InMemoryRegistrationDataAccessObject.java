package data_access;

import entity.User;
import use_case.registration.RegistrationDataAccessInterface;
import utility.exceptions.DuplicateUsernameException;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that implements the RegistrationDataAccessInterface interface.
 * This class manages user registration using an in-memory data storage.
 */
public class InMemoryRegistrationDataAccessObject implements RegistrationDataAccessInterface{

    // In-memory storage for registered users
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) throws DuplicateUsernameException {
        if (users.containsKey(user.getUsername())) {
            throw new DuplicateUsernameException("Username already exists. Please choose another one.");
        }
        users.put(user.getUsername(), user);
    }
}
