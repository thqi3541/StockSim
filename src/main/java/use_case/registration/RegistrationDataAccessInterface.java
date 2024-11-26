package use_case.registration;

import entity.User;

public interface RegistrationDataAccessInterface {
    boolean hasUsername(String username);

    void saveUser(User user);
}
