package use_case.view_history;

import entity.User;
import utility.exceptions.ValidationException;

public interface ViewHistoryDataAccessInterface {
    User getUserWithCredential(String credential) throws ValidationException;
}
