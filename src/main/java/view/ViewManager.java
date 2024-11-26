package view;

import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewManager {
    private static ViewManager instance;
    private final List<IComponent> components = new ArrayList<>();
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private ViewManager() {
    }

    // Ensure thread-safety for singleton instance creation
    public static synchronized ViewManager Instance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void broadcastEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent switchEvent) {
            switchPanel(switchEvent.getPanelName());
            return;
        }

        boolean eventHandled = false;
        for (IComponent component : components) {
            component.receiveViewEvent(event);
            eventHandled = true;
        }

        if (!eventHandled) {
            System.out.println(
                    "Warning: Event "
                            + event.getClass().getSimpleName()
                            + " was broadcasted but not handled by any component.");
        }
    }

    public void registerComponent(IComponent component) {
        if (!components.contains(component)) {
            components.add(component);
        }
    }

    public void setCardLayout(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
    }

    private void switchPanel(String panelName) {
        if (cardLayout == null || cardPanel == null) {
            throw new IllegalStateException("CardLayout or CardPanel is not set up in ViewManager.");
        }
        cardLayout.show(cardPanel, panelName);
        System.out.println("Switched to panel: " + panelName);
    }
}
