package data_access;

import entity.User;
import session.SessionManager;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.registration.RegistrationDataAccessInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that implements the ExecuteBuyDataAccessInterface interface
 * This class is used to get the user with the given credential
 */
public class InMemoryUserDataAccessObject implements ExecuteBuyDataAccessInterface, RegistrationDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        SessionManager sessionManager = SessionManager.Instance();
        String username = sessionManager.getUsername(credential).orElseThrow(ValidationException::new);
        return getUserWithUsername(username);
    }

    @Override
    public void saveUser(User user) {
        // Saves the user to the in-memory storage
        users.put(user.getUsername(), user);
    }

    // TODO: implement this method, it should read from an in-memory database
    public User getUserWithUsername(String username) {
        return users.get(username);
    }
}
