package view;

import view.view_events.EventType;
import view.view_events.ViewEvent;

import java.util.EnumSet;

public interface IComponent {
    /**
     * Handles a received event.
     *
     * @param event The event to handle.
     */
    void receiveViewEvent(ViewEvent event);
    
}