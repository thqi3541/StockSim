package data_access;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.execute_sell.ExecuteSellDataAccessInterface;
import use_case.login.LoginDataAccessInterface;
import use_case.registration.RegistrationDataAccessInterface;
import use_case.view_history.ViewHistoryDataAccessInterface;
import utility.ConfigLoader;
import utility.ServiceManager;
import utility.SessionManager;
import utility.UserCache;
import utility.exceptions.DocumentParsingException;
import utility.exceptions.ValidationException;

import java.util.concurrent.CompletableFuture;


public class CachedDatabaseUserDataAccessObject implements
        RegistrationDataAccessInterface,
        LoginDataAccessInterface,
        ExecuteBuyDataAccessInterface,
        ExecuteSellDataAccessInterface,
        ViewHistoryDataAccessInterface {

    private static final int USER_CACHE_MAX_CAPACITY =
            Integer.parseInt(ConfigLoader.getProperty("config/database-config.txt", "USER_CACHE_MAX_CAPACITY"));

    private final UserCache cachedUsers = new UserCache(USER_CACHE_MAX_CAPACITY);
    private DatabaseUserDataAccessObject database;


    public CachedDatabaseUserDataAccessObject() {
        database = new DatabaseUserDataAccessObject();
        ServiceManager.Instance().registerService(CachedDatabaseUserDataAccessObject.class, this);
        ServiceManager.Instance().registerService(RegistrationDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(LoginDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ExecuteBuyDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ExecuteSellDataAccessInterface.class, this);
        ServiceManager.Instance().registerService(ViewHistoryDataAccessInterface.class, this);
    }

    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        // get username from credential
        String username = SessionManager.Instance().getUsername(credential).orElseThrow(ValidationException::new);
        // search user from cache
        User user = cachedUsers.get(username);
        // search user from database
        if (user == null) {
            user = database.getUserWithCredential(credential);
            cachedUsers.add(user);
        }
        return user;
    }

    @Override
    public void updateUserData(User user) {
        // Asynchronously write data to the database
        CompletableFuture.runAsync(() -> {
            try {
                // Call the database method to update user data
                database.updateUserData(user);
                // Update user data in cache
                cachedUsers.updateUser(user);
            } catch (Exception e) {
                // Handle any exceptions that might occur during database update
                throw new RuntimeException("Failed to update user data", e);
            }
        }).exceptionally(ex -> {
            System.err.println("Error occurred while updating user data: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        });
    }

    @Override
    public User getUserWithPassword(String username, String password) throws ValidationException {
        // search user from cache
        User user = cachedUsers.get(username);
        // search user from database
        if (user == null) {
            user = database.getUserWithPassword(username, password);
            cachedUsers.add(user);
        }
        return user;
    }

    @Override
    public boolean hasUsername(String username) {
        return cachedUsers.get(username) != null || database.hasUsername(username);
    }

    @Override
    public void createUser(User user) throws DocumentParsingException {
        // Asynchronously write data to the database
        CompletableFuture.runAsync(() -> {
            try {
                // Call the database method to update user data
                database.createUser(user);
                cachedUsers.add(user);
            } catch (Exception e) {
                // Handle any exceptions that might occur during database update
                throw new RuntimeException("Failed to update user data", e);
            }
        }).exceptionally(ex -> {
            System.err.println("Error occurred while updating user data: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        });
    }
}