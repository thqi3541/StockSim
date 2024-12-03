package use_case.execute_sell;

import data_access.UserDataAccessInterface;
import entity.User;
import java.rmi.ServerException;
import utility.exceptions.ValidationException;

/** The interface of the DAO for the Execute Sell Use Case. */
public interface ExecuteSellDataAccessInterface extends UserDataAccessInterface {

    /**
     * Gets the current user associated with the given credential.
     *
     * @param credential the user's credential
     * @return the user associated with the credential
     * @throws ValidationException if the provided credential is invalid
     */
    User getUserWithCredential(String credential) throws ValidationException;

    /**
     * Updates the user's data in the database.
     *
     * @param user the user object containing updated data
     * @throws ServerException if an error occurs while updating the data
     */
    void updateUserData(User user) throws ServerException;
}
