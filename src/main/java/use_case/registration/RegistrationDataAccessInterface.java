package use_case.registration;

import entity.User;
import utility.exceptions.DocumentParsingException;

public interface RegistrationDataAccessInterface {
    boolean hasUsername(String username);

    void createUser(User user) throws DocumentParsingException;
}
