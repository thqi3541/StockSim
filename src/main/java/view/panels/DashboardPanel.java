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
import java.util.Date;

public class DashboardPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int MAIN_PADDING = 20;
    private static final int SECTION_SPACING = 10;
    private static final int HEADER_LINE_SPACING = 5;

    // Font Constants
    private static final Font WELCOME_FONT = new Font("Lucida Sans", Font.BOLD, 24);
    private static final Font BALANCE_FONT = new Font("Lucida Sans", Font.PLAIN, 18);

    // Text Constants
    private static final String CURRENCY_FORMAT = "$%.2f";
    private static final String DEFAULT_USERNAME = "Guest";
    private static final double DEFAULT_CASH = 0.0;
    private static final double DEFAULT_POSITION = 0.0;

    // Components
    private final JLabel welcomeLabel;
    private final JLabel balanceLabel;
    private final ButtonComponent tradeButton;
    private final ButtonComponent historyButton;
    private final ButtonComponent logoutButton;

    public DashboardPanel() {
        // Initialize components
        welcomeLabel = new JLabel("Welcome back, " + DEFAULT_USERNAME);
        balanceLabel = new JLabel(formatBalanceText(DEFAULT_CASH, DEFAULT_POSITION));
        tradeButton = new ButtonComponent("Trade");
        historyButton = new ButtonComponent("View Transaction History");
        logoutButton = new ButtonComponent("Log out");

        ViewManager.Instance().registerComponent(this);

        setupMainPanel();

        // Add components to the panel
        add(createHeaderPanel());
        add(Box.createRigidArea(new Dimension(0, SECTION_SPACING)));
        add(createCenterPanel());
        add(Box.createRigidArea(new Dimension(0, SECTION_SPACING)));
        add(createFooterPanel());

        setupButtonActions();
    }

    private void setupMainPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(MAIN_PADDING, MAIN_PADDING, MAIN_PADDING, MAIN_PADDING));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Configure welcome label
        welcomeLabel.setFont(WELCOME_FONT);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Configure balance label
        balanceLabel.setFont(BALANCE_FONT);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add labels with spacing
        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, HEADER_LINE_SPACING)));
        headerPanel.add(balanceLabel);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Trading Management Panel
        JPanel tradingManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tradingManagementPanel.setBorder(BorderFactory.createTitledBorder("Trading management"));
        tradingManagementPanel.add(tradeButton);
        tradingManagementPanel.add(historyButton);

        // Account Management Panel
        JPanel accountManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        accountManagementPanel.setBorder(BorderFactory.createTitledBorder("Account management"));
        accountManagementPanel.add(logoutButton);

        // Combine panels
        centerPanel.add(tradingManagementPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, SECTION_SPACING)));
        centerPanel.add(accountManagementPanel);

        return centerPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        footerPanel.add(new JLabel("Thank you for using StockSim."));
        footerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return footerPanel;
    }

    private String formatBalanceText(double cash, double position) {
        return String.format("You have %s in cash and %s in position.",
                String.format(CURRENCY_FORMAT, cash),
                String.format(CURRENCY_FORMAT, position));
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
        User user = new User("test_user", "password");
        user.addBalance(10000.00);

        Stock testStock1 = new Stock("AAPL", "Apple Inc.", "Technology", 150.00);
        Stock testStock2 = new Stock("GOOGL", "Alphabet Inc.", "Technology", 2800.00);
        user.getPortfolio().addStock(new UserStock(testStock1, 145.00, 10));
        user.getPortfolio().addStock(new UserStock(testStock2, 2750.00, 5));

        Date testDate1 = new Date();
        Date testDate2 = new Date(testDate1.getTime() - 24 * 60 * 60 * 1000);

        Transaction testTransaction1 = new Transaction(testDate1, "AAPL", 10, 150.00, "BUY");
        Transaction testTransaction2 = new Transaction(testDate2, "GOOGL", 5, 2800.00, "BUY");

        user.getTransactionHistory().addTransaction(testTransaction1);
        user.getTransactionHistory().addTransaction(testTransaction2);
        return user;
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateUsernameEvent userEvent) {
            String username = userEvent.getUsername();
            updateDashboardUserName(username);
        } else if (event instanceof UpdateAssetEvent assetEvent) {
            double cash = assetEvent.getBalance();
            double portfolioValue = assetEvent.getPortfolio().getTotalValue();
            updateDashboardValue(cash, portfolioValue);
        }
    }

    private void updateDashboardUserName(String username) {
        welcomeLabel.setText("Welcome back, " + username);
    }

    private void updateDashboardValue(double cash, double portfolioValue) {
        balanceLabel.setText(formatBalanceText(cash, portfolioValue));
    }
}
