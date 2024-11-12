package use_case.execute_buy;

import entity.User;
import utility.ValidationException;

public interface ExecuteBuyDataAccessInterface {
    User getUserWithCredential(String credential) throws ValidationException;

}
