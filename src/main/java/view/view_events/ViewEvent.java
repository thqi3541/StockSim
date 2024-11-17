package view.view_events;

import java.util.EnumSet;

public abstract class ViewEvent {
    private final EnumSet<EventType> types;

    public ViewEvent(EnumSet<EventType> types) {
        this.types = types;
    }
}