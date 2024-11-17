package app;

import data_access.InMemoryUserDataAccessObject;
import data_access.StockDataAccessInterface;
import data_access.StockDataAccessObject;
import interface_adapter.execute_buy.ExecuteBuyController;
import interface_adapter.execute_buy.ExecuteBuyPresenter;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.view_history.ViewHistoryController;
import interface_adapter.view_history.ViewHistoryPresenter;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import use_case.execute_buy.ExecuteBuyInputBoundary;
import use_case.execute_buy.ExecuteBuyInteractor;
import use_case.execute_buy.ExecuteBuyOutputBoundary;
import use_case.login.LoginDataAccessInterface;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.view_history.ViewHistoryDataAccessInterface;
import use_case.view_history.ViewHistoryInputBoundary;
import use_case.view_history.ViewHistoryInteractor;
import use_case.view_history.ViewHistoryOutputBoundary;
import utility.ServiceManager;
import utility.StockMarket;
import utility.ViewManager;
import view.components.DialogComponent;
import view.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A builder class for the application.
 * Manages application frame creation, service registration, and view management.
 */
public class AppBuilder {
    // Default dimensions and title for the application window
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 900;
    private static final String DEFAULT_TITLE = "StockSim";

    // Components for the application
    private final JPanel cardPanel;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> panels;
    private String initialPanel = "LogInPanel";

    // Custom dimensions and title for the application window
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private String title = DEFAULT_TITLE;

    // Store reference to StockDataAccessObject for cleanup
    private StockDataAccessObject stockDAO;

    /**
     * Constructor for the AppBuilder class
     */
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
    public AppBuilder addDashboardPanel() {
        addPanel("DashboardPanel", new DashboardPanel());
        return this;
    }

    /**
     * Adds the action panels
     */
    public AppBuilder addActionPanels() {
        addPanel("TradeSimulationPanel", new TradeSimulationPanel());
        addPanel("TransactionHistoryPanel", new TransactionHistoryPanel());
        return this;
    }

    /**
     * Adds the dialog component
     */
    public AppBuilder addDialogComponent() {
        ServiceManager.Instance().registerService(DialogComponent.class, new DialogComponent());
        return this;
    }

    /**
     * Helper method to add a panel to both the card layout and panels map
     */
    private void addPanel(String name, JPanel panel) {
        panels.put(name, panel);
        cardPanel.add(panel, name);
    }

    /**
     * Initializes all required services
     */
    private void initializeServices() {
        // 1. Initialize DAOs first
        stockDAO = new StockDataAccessObject();
        // TODO: this should be replaced with a real user DAO later
        InMemoryUserDataAccessObject userDAO = new InMemoryUserDataAccessObject();

        // Register stock dao as its interface
        ServiceManager.Instance().registerService(StockDataAccessInterface.class, stockDAO);

        // Initialize and register StockMarket
        StockMarket.Instance().initialize(stockDAO);

        // Register user dao as their interfaces
        ServiceManager.Instance().registerService(InMemoryUserDataAccessObject.class, userDAO);
        ServiceManager.Instance().registerService(ExecuteBuyDataAccessInterface.class, userDAO);
        ServiceManager.Instance().registerService(ViewHistoryDataAccessInterface.class, userDAO);
        ServiceManager.Instance().registerService(LoginDataAccessInterface.class, userDAO);

        // 2. Initialize Presenters and register them as output boundary interfaces
        ExecuteBuyOutputBoundary buyPresenter = new ExecuteBuyPresenter();
        ViewHistoryOutputBoundary viewHistoryPresenter = new ViewHistoryPresenter();
        LoginOutputBoundary loginPresenter = new LoginPresenter();

        ServiceManager.Instance().registerService(ExecuteBuyOutputBoundary.class, buyPresenter);
        ServiceManager.Instance().registerService(ViewHistoryOutputBoundary.class, viewHistoryPresenter);
        ServiceManager.Instance().registerService(LoginOutputBoundary.class, loginPresenter);

        // 3. Initialize Interactors and register them as input boundary interfaces
        ExecuteBuyInputBoundary buyInteractor = new ExecuteBuyInteractor(
                ServiceManager.Instance().getService(ExecuteBuyDataAccessInterface.class),
                ServiceManager.Instance().getService(ExecuteBuyOutputBoundary.class)
        );
        ViewHistoryInputBoundary viewHistoryInteractor = new ViewHistoryInteractor(
                ServiceManager.Instance().getService(ViewHistoryDataAccessInterface.class),
                ServiceManager.Instance().getService(ViewHistoryOutputBoundary.class)
        );
        LoginInputBoundary loginInteractor = new LoginInteractor(
                ServiceManager.Instance().getService(LoginDataAccessInterface.class),
                ServiceManager.Instance().getService(LoginOutputBoundary.class)
        );

        ServiceManager.Instance().registerService(ExecuteBuyInputBoundary.class, buyInteractor);
        ServiceManager.Instance().registerService(ViewHistoryInputBoundary.class, viewHistoryInteractor);
        ServiceManager.Instance().registerService(LoginInputBoundary.class, loginInteractor);

        // 4. Initialize Controllers
        ServiceManager.Instance().registerService(ExecuteBuyController.class, new ExecuteBuyController(
                ServiceManager.Instance().getService(ExecuteBuyInputBoundary.class))
        );
        ServiceManager.Instance().registerService(ViewHistoryController.class, new ViewHistoryController(
                ServiceManager.Instance().getService(ViewHistoryInputBoundary.class))
        );
        ServiceManager.Instance().registerService(LoginController.class, new LoginController(
                ServiceManager.Instance().getService(LoginInputBoundary.class))
        );
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
