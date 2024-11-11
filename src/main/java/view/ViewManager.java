package view;

import view.view_event.SwitchPanelEvent;
import view.view_event.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    private static ViewManager instance;  // Singleton instance
    private final List<IComponent> components = new ArrayList<>();

    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Private constructor to prevent instantiation
    private ViewManager() {
    }

    /**
     * Returns the singleton instance of ViewManager.
     */
    public static ViewManager Instance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    /**
     * Sets the CardLayout and JPanel used for switching between panels.
     *
     * @param cardLayout The CardLayout used for panel switching.
     * @param cardPanel  The JPanel that holds all the panels.
     */
    public void setCardLayout(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    /**
     * Registers a component to receive view events.
     *
     * @param component The component implementing IComponent interface.
     */
    public void registerComponent(IComponent component) {
        components.add(component);
    }

    /**
     * Broadcasts an event to all registered components.
     *
     * @param event The event to broadcast.
     */
    public void broadcastEvent(ViewEvent event) {
        // Check if the event is a SwitchPanelEvent first and handle switching internally
        if (event instanceof SwitchPanelEvent) {
            switchPanel(((SwitchPanelEvent) event).getPanelName());
        }

        // Distribute the event to components that support any of the event's types
        components.stream()
                .filter(component -> event.getTypes().stream().anyMatch(component::supportsEvent))
                .forEach(component -> component.receiveViewEvent(event));
    }

    /**
     * Switches the displayed panel in the CardLayout.
     *
     * @param panelName The name of the panel to show.
     */
    public void switchPanel(String panelName) {
        if (cardLayout != null && cardPanel != null) {
            cardLayout.show(cardPanel, panelName);
        } else {
            System.err.println("Error: CardLayout or CardPanel is not set up in ViewManager.");
        }
    }
}