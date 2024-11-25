package view;

import view.view_events.ViewEvent;

public interface IComponent {
  /**
   * Handles a received event.
   *
   * @param event The event to handle.
   */
  void receiveViewEvent(ViewEvent event);
}
