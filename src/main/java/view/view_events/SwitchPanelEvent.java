package view.view_events;

public class SwitchPanelEvent extends ViewEvent {
    private final String panelName;

    /**
     * Creates a SwitchPanelEvent with only the SWITCH_PANEL type.
     *
     * @param panelName The name of the panel to switch to.
     */
    public SwitchPanelEvent(String panelName) {
        this.panelName = panelName;
    }

    /**
     * Returns the name of the panel to switch to.
     *
     * @return The panel name.
     */
    public String getPanelName() {
        return panelName;
    }
}
