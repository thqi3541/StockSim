package use_case.execute_sell;

import data_access.UserDataAccessInterface;
import entity.User;
import utility.exceptions.ValidationException;

import java.rmi.ServerException;

public interface ExecuteSellDataAccessInterface
        extends UserDataAccessInterface {

    User getUserWithCredential(String credential) throws ValidationException;

    void updateUserData(User user) throws ServerException;
}
