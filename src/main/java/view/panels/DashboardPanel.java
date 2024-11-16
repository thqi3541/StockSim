package view.panels;

import entity.Stock;
import entity.Transaction;
import entity.User;
import entity.UserStock;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

public class DashboardPanel extends JPanel implements IComponent {
    // Constants
    private static final Font WELCOME_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font BALANCE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final int BORDER_PADDING = 10;
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 400;
    private static final int PREF_WIDTH = 500;
    private static final int PREF_HEIGHT = 400;
    private static final String CURRENCY_FORMAT = "$%.2f";

    // Default placeholder values
    private static final String DEFAULT_USERNAME = "Guest";
    private static final double DEFAULT_CASH = 0.0;
    private static final double DEFAULT_POSITION = 0.0;

    // Date Format Constants
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    private final JLabel welcomeLabel;
    private final JLabel balanceLabel;
    private final ButtonComponent tradeButton;
    private final ButtonComponent historyButton;
    private final ButtonComponent logoutButton;

    public DashboardPanel() {
        // Initialize all buttons first
        tradeButton = new ButtonComponent("Trade");
        historyButton = new ButtonComponent("View Transaction History");
        logoutButton = new ButtonComponent("Log out");

        // Initialize labels
        welcomeLabel = new JLabel("Welcome back, " + DEFAULT_USERNAME);
        balanceLabel = new JLabel(formatBalanceText(DEFAULT_CASH, DEFAULT_POSITION));

        ViewManager.Instance().registerComponent(this);
        setupPanelLayout();
        setupWelcomePanel();
        add(createManagementPanel(), BorderLayout.CENTER);
        setupButtonActions();
    }

    private void setupWelcomePanel() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        welcomeLabel.setFont(WELCOME_FONT);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        balanceLabel.setFont(BALANCE_FONT);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(balanceLabel);
        add(welcomePanel, BorderLayout.NORTH);
    }

    private void setupButtonActions() {
        tradeButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TradeSimulationPanel")));

        historyButton.addActionListener(e -> {
            User testUser = createDevelopmentTestUser();
            ViewManager.Instance().broadcastEvent(new UpdateTransactionHistoryEvent(testUser.getTransactionHistory()));
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TransactionHistoryPanel"));
        });

        logoutButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel")));
    }

    private User createDevelopmentTestUser() {
        // Clear documentation that this is for development testing
        System.out.println("WARNING: Using development test user data");

        User user = new User("test_user", "password");
        user.addBalance(10000.00);

        // Create test portfolio with clearly marked test data
        Stock testStock1 = new Stock("AAPL", "Apple Inc.", "Technology", 150.00);
        Stock testStock2 = new Stock("GOOGL", "Alphabet Inc.", "Technology", 2800.00);
        user.getPortfolio().addStock(new UserStock(testStock1, 145.00, 10));
        user.getPortfolio().addStock(new UserStock(testStock2, 2750.00, 5));

        // Create transactions with formatted dates
        Date testDate1 = new Date();
        Date testDate2 = new Date(testDate1.getTime() - 24 * 60 * 60 * 1000); // One day before

        Transaction testTransaction1 = new Transaction(testDate1, "AAPL", 10, 150.00, "BUY");
        Transaction testTransaction2 = new Transaction(testDate2, "GOOGL", 5, 2800.00, "BUY");

        user.getTransactionHistory().addTransaction(testTransaction1);
        user.getTransactionHistory().addTransaction(testTransaction2);
        return user;
    }

    private String formatBalanceText(double cash, double position) {
        return String.format("You have %s in cash and %s in position",
                String.format(CURRENCY_FORMAT, cash),
                String.format(CURRENCY_FORMAT, position));
    }

    private void setupPanelLayout() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
    }

    private JPanel createManagementPanel() {
        // Trading Management Panel
        JPanel tradingManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tradingManagementPanel.setBorder(BorderFactory.createTitledBorder("Trading management"));

        tradingManagementPanel.add(tradeButton);
        tradingManagementPanel.add(historyButton);

        // Account Management Panel
        JPanel accountManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        accountManagementPanel.setBorder(BorderFactory.createTitledBorder("Account management"));
        accountManagementPanel.add(logoutButton);

        // Combine Panels
        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(new BoxLayout(managementPanel, BoxLayout.Y_AXIS));
        managementPanel.add(tradingManagementPanel);
        managementPanel.add(Box.createVerticalStrut(10));
        managementPanel.add(accountManagementPanel);

        return managementPanel;
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent) {
            System.out.println("DashboardPanel received a SwitchPanelEvent.");
        } else if (event instanceof UpdateCurrentUserEvent userEvent) {
            User user = userEvent.getUser();
            double portfolioValue = user.getPortfolio() != null ? user.getPortfolio().getTotalValue() : 0.0;
            updateDashboard(user.getUsername(), user.getBalance(), portfolioValue);
        }
    }

    private void updateDashboard(String username, double cash, double position) {
        welcomeLabel.setText("Welcome back, " + username);
        balanceLabel.setText(formatBalanceText(cash, position));
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.SWITCH_PANEL, EventType.UPDATE_CURRENT_USER);
    }
}