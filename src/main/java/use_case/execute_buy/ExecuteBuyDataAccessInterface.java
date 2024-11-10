package use_case.execute_buy;

import entity.User;

public interface ExecuteBuyDataAccessInterface {
    User getUserWithCredential(String credential) throws ValidationError;

    // TODO: define this exception elsewhere
    class ValidationError extends Exception {
    }
}
