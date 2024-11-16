package view.panels;

import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.EventType;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;
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

    private final JLabel welcomeLabel;
    private final ButtonComponent tradeButton;
    private final ButtonComponent historyButton;
    private final ButtonComponent logoutButton;

    public DashboardPanel(String username, double cash, double position) {
        ViewManager.Instance().registerComponent(this);
        setupPanelLayout();

        // Welcome Panel (contains both labels)
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));

        welcomeLabel = new JLabel("Welcome back, " + username);
        welcomeLabel.setFont(WELCOME_FONT);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Balance Label
        String balanceText = String.format("You have %s in cash and %s in position",
                String.format(CURRENCY_FORMAT, cash),
                String.format(CURRENCY_FORMAT, position));
        balanceLabel = new JLabel(balanceText);
        balanceLabel.setFont(BALANCE_FONT);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add labels to welcome panel with spacing
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(balanceLabel);
        add(welcomePanel, BorderLayout.NORTH);

        // Add management panels
        add(createManagementPanel(), BorderLayout.CENTER);
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

        // Trade Button
        tradeButton = new ButtonComponent("Trade");
        tradeButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TradeSimulationPanel")));
        tradingManagementPanel.add(tradeButton);

        // View Transaction History Button with ActionListener for switching panels
        historyButton = new ButtonComponent("View Transaction History");
        historyButton.addActionListener(e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TransactionHistoryPanel")));
        tradingManagementPanel.add(historyButton);

        // Account Management Panel
        JPanel accountManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        accountManagementPanel.setBorder(BorderFactory.createTitledBorder("Account management"));

        // Logout Button
        logoutButton = new ButtonComponent("Log out");
        logoutButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel")));
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
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.SWITCH_PANEL);
    }
}
