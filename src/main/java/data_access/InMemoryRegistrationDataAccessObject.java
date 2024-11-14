package data_access;

import entity.User;
import use_case.registration.RegistrationDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that implements the RegistrationDataAccessInterface interface.
 * This class manages user registration using an in-memory data storage.
 */
public class InMemoryRegistrationDataAccessObject implements RegistrationDataAccessInterface {

    // In-memory storage for registered users
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        // Saves the user to the in-memory storage
        users.put(user.getUsername(), user);
    }

    @Override
    public User getUserWithUsername(String username) {
        // Retrieves a user by username, or returns null if not found
        return users.get(username);
    }
}
