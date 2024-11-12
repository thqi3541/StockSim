package session;

import java.util.Optional;


public class ClientSessionManager {

    // singleton instance
    private volatile static ClientSessionManager instance;

    // credential for the session
    private String credential;

    // singleton constructor
    private ClientSessionManager() { }

    public static synchronized ClientSessionManager Instance() {
        if (instance == null) {
            instance = new ClientSessionManager();
        }
        return instance;
    }

    public Optional<String> getCredential() {
        return Optional.ofNullable(credential);
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

}
