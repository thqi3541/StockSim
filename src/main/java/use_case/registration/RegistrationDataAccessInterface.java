package use_case.registration;

import entity.User;
import utility.exceptions.DocumentParsingException;

public interface RegistrationDataAccessInterface {

  boolean hasUsername(String username);

  void saveUser(User user) throws DocumentParsingException;
}
