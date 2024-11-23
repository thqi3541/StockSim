package use_case.login;

import entity.User;
import utility.exceptions.ValidationException;

public interface LoginDataAccessInterface {

    User getUserWithPassword(String username, String password) throws ValidationException;
}
