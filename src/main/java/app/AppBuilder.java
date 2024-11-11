package app;

import view.ViewManager;
import view.main_panels.authentication.LogInPanel;
import view.main_panels.authentication.SignUpPanel;
import view.main_panels.dashboard.DashboardPanel;
import view.main_panels.trade_simulation.TradeSimulationPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A builder class for the application.
 * This class now works as a setup utility to add panels to ViewManager and build the main application frame.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Add the login and sign-up panels to the application
     *
     * @return the builder
     */
    public AppBuilder addAuthenticationPanels() {
        LogInPanel logInPanel = new LogInPanel();
        SignUpPanel signUpPanel = new SignUpPanel();

        // Register panels with ViewManager
        ViewManager.Instance().registerComponent(logInPanel);
        ViewManager.Instance().registerComponent(signUpPanel);

        // Add panels to the card layout
        cardPanel.add(logInPanel, "LogInPanel");
        cardPanel.add(signUpPanel, "SignUpPanel");

        return this;
    }

    /**
     * Add the dashboard panel to the application
     *
     * @return the builder
     */
    public AppBuilder addDashboardPanel(String username, double cash, double position) {
        DashboardPanel dashboardPanel = new DashboardPanel(username, cash, position);

        // Register the dashboard panel with ViewManager
        ViewManager.Instance().registerComponent(dashboardPanel);

        // Add the dashboard panel to the card layout
        cardPanel.add(dashboardPanel, "DashboardPanel");

        return this;
    }

    /**
     * Add the trade simulation panel to the application
     *
     * @return the builder
     */
    public AppBuilder addTradeSimulationPanel() {
        TradeSimulationPanel tradeSimulationPanel = new TradeSimulationPanel();

        // Register the trade simulation panel with ViewManager
        ViewManager.Instance().registerComponent(tradeSimulationPanel);

        // Add the trade simulation panel to the card layout
        cardPanel.add(tradeSimulationPanel, "TradeSimulationPanel");

        return this;
    }

    /**
     * Build the application frame and show the LogInPanel initially
     *
     * @return the application frame
     */
    public JFrame build() {
        JFrame application = new JFrame("Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1000, 800);
        application.add(cardPanel);

        // Set ViewManager to control panel switching with cardLayout and cardPanel
        ViewManager.Instance().setCardLayout(cardLayout, cardPanel);

        // Show the LogInPanel initially
        cardLayout.show(cardPanel, "LogInPanel");

        return application;
    }
}