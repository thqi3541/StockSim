package use_case.view_history;

import data_access.UserDataAccessInterface;
import entity.User;
import utility.exceptions.ValidationException;

/**
 * The interface of the DAO for the View Transaction History Use Case.
 */
public interface ViewHistoryDataAccessInterface
        extends UserDataAccessInterface {

    /**
     * Gets the current user from the user credential and throws ValidationException if credential is
     * invalid
     *
     * @param credential the user's credential
     * @return the user associated with the credential
     * @throws ValidationException if provided credential is invalid
     */
    User getUserWithCredential(String credential) throws ValidationException;
}
