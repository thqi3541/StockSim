package use_case.execute_buy;

import data_access.UserDataAccessInterface;
import entity.User;
import utility.exceptions.ValidationException;

public interface ExecuteBuyDataAccessInterface extends UserDataAccessInterface {

  User getUserWithCredential(String credential) throws ValidationException;
}
