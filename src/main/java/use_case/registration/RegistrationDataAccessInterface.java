package use_case.registration;

import entity.User;
import utility.exceptions.DuplicateUsernameException;

public interface RegistrationDataAccessInterface {
    void saveUser(User user) throws DuplicateUsernameException;
}