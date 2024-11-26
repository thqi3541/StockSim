package view.view_events;

public class UpdateUsernameEvent extends ViewEvent {

  private final String username;

  public UpdateUsernameEvent(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
