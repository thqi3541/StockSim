package data_access;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.login.LoginDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;
import utility.ServiceManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;

import java.util.Map;

/**
 * A class that implements the ExecuteBuyDataAccessInterface interface
 * This class is used to get the user with the given credential
 */
public class InMemoryUserDataAccessObject implements ExecuteBuyDataAccessInterface, ViewHistoryDataAccessInterface, LoginDataAccessInterface {
    private static final String DEFAULT_PASSWORD = "000"; // Set all passwords to "000"

    private final Map<String, User> users;

    public InMemoryUserDataAccessObject() {
        this.users = new java.util.HashMap<>();

        // Initialize with predefined users
        User user1 = new User("user1", DEFAULT_PASSWORD);
        user1.addBalance(100000.00);
        users.put("user1", user1);

        User user2 = new User("user2", DEFAULT_PASSWORD);
        user2.addBalance(200000.00);
        users.put("user2", user2);

        User user3 = new User("user3", DEFAULT_PASSWORD);
        user3.addBalance(300000.00);
        users.put("user3", user3);

        ServiceManager.Instance().registerService(ExecuteBuyDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ViewHistoryDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(LoginDataAccessInterface.class, this);
    }

    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        SessionManager sessionManager = SessionManager.Instance();
        String username = sessionManager.getUsername(credential).orElseThrow(ValidationException::new);
        return getUserWithUsername(username);
    }

    // TODO: should we throw a different exception if the user is not found?
    private User getUserWithUsername(String username) throws ValidationException {
        User user = users.get(username);
        if (user == null) {
            throw new ValidationException();
        }
        return user;
    }

    @Override
    public User getUserWithPassword(String username, String password) throws ValidationException {
        User user = users.get(username);

        if (user == null) {
            throw new ValidationException();
        }

        // TODO: should we throw a different exception if the password does not match?
        if (!user.getPassword().equals(password)) {
            throw new ValidationException();
        }

        return user;
    }
}