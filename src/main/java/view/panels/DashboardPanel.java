package view.panels;

import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.*;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class DashboardPanel extends JPanel implements IComponent {
    private final JLabel welcomeLabel;
    private final ButtonComponent tradeButton;
    private final ButtonComponent historyButton;
    private final ButtonComponent logoutButton;

    private String displayUsername;
    private double displayCash;
    private double displayPortfolio;

    public DashboardPanel(String username, double cash, double portfolio) {
        ViewManager.Instance().registerComponent(this);

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 400));
        setPreferredSize(new Dimension(500, 400));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Welcome Label
        this.displayUsername = username;
        this.displayCash = cash;
        this.displayPortfolio = portfolio;
        welcomeLabel = new JLabel();
        updateWelcomeLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setVerticalAlignment(SwingConstants.TOP);
        welcomeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(welcomeLabel, BorderLayout.NORTH);

        // Trading Management Panel
        JPanel tradingManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tradingManagementPanel.setBorder(BorderFactory.createTitledBorder("Trading management"));

        // Trade Button with ActionListener for switching panels
        tradeButton = new ButtonComponent("Trade");
        tradeButton.addActionListener(e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TradeSimulationPanel")));
        tradingManagementPanel.add(tradeButton);

        // View Transaction History Button with ActionListener for switching panels
        historyButton = new ButtonComponent("View Transaction History");
        historyButton.addActionListener(e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TransactionHistoryPanel")));
        tradingManagementPanel.add(historyButton);

        // Account Management Panel
        JPanel accountManagementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        accountManagementPanel.setBorder(BorderFactory.createTitledBorder("Account management"));

        // Logout Button with ActionListener for switching to LogInPanel
        logoutButton = new ButtonComponent("Log out");
        logoutButton.addActionListener(e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel")));
        accountManagementPanel.add(logoutButton);

        // Combine Panels into a Management Panel
        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(new BoxLayout(managementPanel, BoxLayout.Y_AXIS));
        managementPanel.add(tradingManagementPanel);
        managementPanel.add(Box.createVerticalStrut(10));
        managementPanel.add(accountManagementPanel);

        add(managementPanel, BorderLayout.CENTER);
    }

    private void updateWelcomeLabel() {
        welcomeLabel.setText(String.format("<html>Welcome back, %s.<br>You have $%.2f in cash and $%.2f in portfolio.</html>",
                displayUsername,
                displayCash,
                displayPortfolio)
        );
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateUsernameEvent updateUsernameEvent) {
            displayUsername = updateUsernameEvent.getUsername();
            updateWelcomeLabel();
        } else if (event instanceof UpdateAssetEvent updateAssetEvent){
            displayCash = updateAssetEvent.getBalance();
            displayPortfolio = updateAssetEvent.getPortfolio().getTotalValue();
        }
    }

}