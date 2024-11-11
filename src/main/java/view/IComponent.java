package view;

import view.view_event.ViewEvent;

// TODO: learn how to handle events and new data
public interface IComponent {
    void receiveViewEvent(ViewEvent event);
}
