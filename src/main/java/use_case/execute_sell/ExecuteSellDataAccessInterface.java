package use_case.execute_sell;

import data_access.UserDataAccessInterface;
import entity.User;
import java.rmi.ServerException;
import utility.exceptions.ValidationException;

public interface ExecuteSellDataAccessInterface extends UserDataAccessInterface {

    User getUserWithCredential(String credential) throws ValidationException;

    void updateUserData(User user) throws ServerException;
}
