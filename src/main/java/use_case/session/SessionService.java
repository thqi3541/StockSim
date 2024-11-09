package use_case.session;

public interface SessionService {

    // TODO: should use singleton pattern

    void createSession(String username);

    void endSession();

    String getCurrentUsername();
}
