package use_case.execute_buy;

import data_access.UserDataAccessInterface;
import entity.User;
import utility.exceptions.DocumentParsingException;
import utility.exceptions.ValidationException;

import java.rmi.ServerException;

public interface ExecuteBuyDataAccessInterface extends UserDataAccessInterface {
    User getUserWithCredential(String credential) throws ValidationException;
    void updateUserData(User user) throws ServerException;
}
