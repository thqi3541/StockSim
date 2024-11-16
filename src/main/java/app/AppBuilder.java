package app;

import data_access.InMemoryStockDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import interface_adapter.execute_buy.ExecuteBuyController;
import interface_adapter.execute_buy.ExecuteBuyPresenter;
import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryPresenter;
import use_case.execute_buy.ExecuteBuyInputBoundary;
import use_case.execute_buy.ExecuteBuyInteractor;
import use_case.view_history.ViewHistoryInputBoundary;
import use_case.view_history.ViewHistoryInteractor;
import utility.ServiceManager;
import utility.ViewManager;
import view.components.DialogComponent;
import view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder class for the application.
 * Manages frame creation, service registration, and view management.
 */
public class AppBuilder {
    // Default dimensions and title for the application window
    private static final int DEFAULT_WIDTH = 1000;
    private static final int DEFAULT_HEIGHT = 800;
    private static final String DEFAULT_TITLE = "Application";

    // Components for the application
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> panels;
    private String initialPanel = "LogInPanel";

    // Custom dimensions and title for the application window
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private String title = DEFAULT_TITLE;

    public AppBuilder() {
        this.cardPanel = new JPanel();
        this.cardLayout = new CardLayout();
        this.panels = new HashMap<>();
        this.cardPanel.setLayout(cardLayout);
    }

    /**
     * Sets custom dimensions for the application window
     */
    public AppBuilder withDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Sets custom title for the application window
     */
    public AppBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the initial panel to be displayed
     */
    public AppBuilder withInitialPanel(String panelName) {
        this.initialPanel = panelName;
        return this;
    }

    /**
     * Adds authentication panels (Login and SignUp)
     */
    public AppBuilder addAuthenticationPanels() {
        addPanel("LogInPanel", new LogInPanel());
        addPanel("SignUpPanel", new SignUpPanel());
        return this;
    }

    /**
     * Adds the dashboard panel with user information
     */
    public AppBuilder addDashboardPanel(String username, double cash, double position) {
        addPanel("DashboardPanel", new DashboardPanel(username, cash, position));
        return this;
    }

    /**
     * Adds the trade simulation panel
     */
    public AppBuilder addTradeSimulationPanel() {
        addPanel("TradeSimulationPanel", new TradeSimulationPanel());
        return this;
    }

    /**
     * Adds the dialog component
     */
    public void addDialogComponent() {
        ServiceManager.Instance().registerService(DialogComponent.class, new DialogComponent());
    }

    /**
     * Helper method to add a panel to both the card layout and panels map
     */
    private void addPanel(String name, JPanel panel) {
        panels.put(name, panel);
        cardPanel.add(panel, name);
    }

    /**
     * Add the transaction history panel to the application
     *
     * @return
     */
    public AppBuilder addTransactionHistoryPanel() {
        TransactionHistoryPanel transactionHistoryPanel = new TransactionHistoryPanel();
        // Add the transaction history panel to the card layout
        cardPanel.add(transactionHistoryPanel, "TransactionHistoryPanel");
        return this;
    }

    /**
     * Initializes all required services
     */
    private void initializeServices() {
        // Initialize in memory DAOs
        ServiceManager.Instance().registerService(InMemoryStockDataAccessObject.class,
                new InMemoryStockDataAccessObject());
        ServiceManager.Instance().registerService(InMemoryUserDataAccessObject.class,
                new InMemoryUserDataAccessObject());

        // Initialize Presenter
        ExecuteBuyPresenter executeBuyPresenter = new ExecuteBuyPresenter();
        ServiceManager.Instance().registerService(ExecuteBuyPresenter.class, executeBuyPresenter);

        // Initialize Interactor
        InMemoryUserDataAccessObject userDAO = ServiceManager.Instance().getService(InMemoryUserDataAccessObject.class);
        ExecuteBuyInputBoundary executeBuyInteractor = new ExecuteBuyInteractor(userDAO, executeBuyPresenter);

        // Initialize Controller
        ServiceManager.Instance().registerService(ExecuteBuyController.class,
                new ExecuteBuyController(executeBuyInteractor));
    }

    /**
     * Builds and returns the configured application frame
     */
    public JFrame build() {
        // Initialize services
        initializeServices();

        // Configure view manager
        ViewManager.Instance().setCardLayout(cardLayout, cardPanel);

        // Create and configure the main frame
        JFrame application = new JFrame(title);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(width, height);
        application.add(cardPanel);

        // Show initial panel
        cardLayout.show(cardPanel, initialPanel);

        return application;
    }
}
