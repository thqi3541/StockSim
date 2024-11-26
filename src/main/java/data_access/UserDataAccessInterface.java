package data_access;

import entity.User;
import utility.exceptions.ValidationException;

public interface UserDataAccessInterface {

  User getUserWithCredential(String credential) throws ValidationException;
}
