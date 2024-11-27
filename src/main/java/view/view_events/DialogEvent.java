package view.view_events;

public class DialogEvent extends ViewEvent {

    private final String title;
    private final String message;

    public DialogEvent(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
