package view.view_events;

import java.util.EnumSet;

public class UpdateUsernameEvent extends ViewEvent {

    private final String username;

    public UpdateUsernameEvent(String username) {
        super(EnumSet.of(EventType.UPDATE_USERNAME));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
