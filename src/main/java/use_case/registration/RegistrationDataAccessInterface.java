package use_case.registration;

import entity.User;

public interface RegistrationDataAccessInterface {
    void saveUser(User user);
    User getUserWithUsername(String username);
}