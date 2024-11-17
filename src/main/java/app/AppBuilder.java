package app;

import data_access.InMemoryStockDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import data_access.StockDataAccessInterface;
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
import utility.ViewManager;
import view.components.DialogComponent;
import view.panels.*;

import javax.swing.*;
import java.awt.*;

// TODO: better ways of import all the services and panels
// TODO: maybe create a config file for the building process

/**
 * A builder class for the application.
 * This class now works as a setup utility to add panels to ViewManager, build the main application frame, and manage the ServiceLocator.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    // Internal ServiceLocator for managing controllers, interactors, DAOs, and presenters
    private final ServiceManager serviceManager = new ServiceManager();

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

        // Add the trade simulation panel to the card layout
        cardPanel.add(tradeSimulationPanel, "TradeSimulationPanel");

        return this;
    }

    public AppBuilder addDialogComponent() {
        DialogComponent dialogComponent = new DialogComponent();
        return this;
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
     * Build the application frame, initialize controllers, interactors, DAOs, and presenters, and register them in ServiceLocator.
     *
     * @return the application frame
     */
    public JFrame build() {
        JFrame application = new JFrame("Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(1000, 800);
        application.add(cardPanel);

        // Initialize and register stock data access object
        InMemoryStockDataAccessObject stockDataAccessObject = new InMemoryStockDataAccessObject();
        ServiceManager.registerService(StockDataAccessInterface.class, stockDataAccessObject);

        // Initialize and register user data access object
        InMemoryUserDataAccessObject userDataAccessObject = new InMemoryUserDataAccessObject();
        ServiceManager.registerService(ExecuteBuyDataAccessInterface.class, userDataAccessObject);
        ServiceManager.registerService(ViewHistoryDataAccessInterface.class, userDataAccessObject);
        ServiceManager.registerService(LoginDataAccessInterface.class, userDataAccessObject);

        // Initialize and register presenter classes (output boundary) for each use case
        ExecuteBuyOutputBoundary executeBuyPresenter = new ExecuteBuyPresenter();
        ViewHistoryOutputBoundary viewHistoryPresenter = new ViewHistoryPresenter();
        LoginOutputBoundary loginPresenter = new LoginPresenter();

        ServiceManager.registerService(ExecuteBuyOutputBoundary.class, executeBuyPresenter);
        ServiceManager.registerService(ViewHistoryOutputBoundary.class, viewHistoryPresenter);
        ServiceManager.registerService(LoginOutputBoundary.class, loginPresenter);

        // Initialize use case interactors
        ExecuteBuyInputBoundary executeBuyInteractor = new ExecuteBuyInteractor(userDataAccessObject, executeBuyPresenter);
        ViewHistoryInputBoundary viewHistoryInteractor = new ViewHistoryInteractor(userDataAccessObject, viewHistoryPresenter);
        LoginInputBoundary loginInteractor = new LoginInteractor(userDataAccessObject, loginPresenter);

        // Initialize and register controller classes (input boundary) for each use case
        ExecuteBuyController executeBuyController = new ExecuteBuyController(executeBuyInteractor);
        ViewHistoryController viewHistoryController = new ViewHistoryController(viewHistoryInteractor);
        LoginController loginController = new LoginController(loginInteractor);

        ServiceManager.registerService(ExecuteBuyController.class, executeBuyController);
        ServiceManager.registerService(ViewHistoryController.class, viewHistoryController);
        ServiceManager.registerService(LoginController.class, loginController);

        // Set ViewManager to control panel switching with cardLayout and cardPanel
        ViewManager.Instance().setCardLayout(cardLayout, cardPanel);

        // Show the LogInPanel initially
        cardLayout.show(cardPanel, "LogInPanel");

        return application;
    }
}