package view.view_events;

import entity.User;

import java.util.EnumSet;

public class UpdateCurrentUserEvent extends ViewEvent {
    private final User user;

    public UpdateCurrentUserEvent(User user) {
        super(EnumSet.of(EventType.UPDATE_CURRENT_USER));
        this.user = user;
    }

    public UpdateCurrentUserEvent(User user, EnumSet<EventType> types) {
        super(types);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
