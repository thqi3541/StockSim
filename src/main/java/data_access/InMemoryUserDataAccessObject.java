package data_access;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.session.SessionManager;

public class InMemoryUserDataAccessObject implements ExecuteBuyDataAccessInterface {
    @Override
    public User getUserWithCredential(String credential) throws ValidationException {
        SessionManager sessionManager = SessionManager.Instance();
        String username = sessionManager.getUsername(credential).orElseThrow(ValidationException::new);
        return getUserWithUsername(username);
    }

    // TODO: implement this method
    private User getUserWithUsername(String username) {
        return new User(username, "0");
    }
}
