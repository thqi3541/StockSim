package data_access;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.execute_sell.ExecuteSellDataAccessInterface;
import use_case.login.LoginDataAccessInterface;
import use_case.registration.RegistrationDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;
import utility.ServiceManager;
import utility.SessionManager;
import utility.exceptions.ValidationException;

import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that implements the data access interfaces for different use cases
 * This class is used to get the user with the given credential
 */
public class InMemoryUserDataAccessObject implements
        LoginDataAccessInterface,
        ExecuteBuyDataAccessInterface,
        ExecuteSellDataAccessInterface,
        ViewHistoryDataAccessInterface,
        RegistrationDataAccessInterface
{
    private static final String DEFAULT_PASSWORD = "0";

    private final Map<String, User> users;

    public InMemoryUserDataAccessObject() {
        this.users = new HashMap<>();

        // Initialize with predefined users
        User user1 = new User("1", DEFAULT_PASSWORD);
        user1.addBalance(100000.00);
        users.put("1", user1);

        User user2 = new User("2", DEFAULT_PASSWORD);
        user2.addBalance(200000.00);
        users.put("2", user2);

        User user3 = new User("3", DEFAULT_PASSWORD);
        user3.addBalance(300000.00);
        users.put("3", user3);

        ServiceManager.Instance().registerService(InMemoryUserDataAccessObject.class, this);
        ServiceManager.Instance().registerService(RegistrationDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(LoginDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ExecuteBuyDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ExecuteSellDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ViewHistoryDataAccessInterface.class, this);
    }

    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        SessionManager sessionManager = SessionManager.Instance();
        String username = sessionManager.getUsername(credential).orElseThrow(ValidationException::new);
        return getUserWithUsername(username);
    }

    @Override
    public void updateUserData(User user) throws ServerException {
        // in memory data access does not need to explicit update user as User objects are modified by interactor directly.
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

    @Override
    public boolean hasUsername(String username) {
        return users.containsKey(username);
    }

    @Override
    public void createUser(User user) {
        users.put(user.getUsername(), user);
    }
}
