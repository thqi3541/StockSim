package view.main_panels.dashboard;

import view.IComponent;
import view.ViewManager;
import view.view_event.EventType;
import view.view_event.SwitchPanelEvent;
import view.view_event.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class DashboardPanel extends JPanel implements IComponent {
    private final JLabel welcomeLabel;
    private final JButton tradeButton;
    private final JButton logoutButton;

    public DashboardPanel(String username, double cash, double position) {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 400));
        setPreferredSize(new Dimension(500, 400));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        welcomeLabel = new JLabel("<html>Welcome back, " + username + ".<br>You have $" + cash + " in cash and $" + position + " in position.</html>");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setVerticalAlignment(SwingConstants.TOP);
        welcomeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(welcomeLabel, BorderLayout.NORTH);

        // Trading Management Panel
        JPanel tradingManagementPanel = new JPanel();
        tradingManagementPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tradingManagementPanel.setBorder(BorderFactory.createTitledBorder("Trading management"));

        tradeButton = new JButton("Trade");
        tradeButton.setBackground(Color.LIGHT_GRAY);
        tradeButton.setForeground(Color.BLACK);

        // Action listener to switch to TradeSimulationPanel
        tradeButton.addActionListener(e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TradeSimulationPanel")));

        tradingManagementPanel.add(tradeButton);

        // Account Management Panel
        JPanel accountManagementPanel = new JPanel();
        accountManagementPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        accountManagementPanel.setBorder(BorderFactory.createTitledBorder("Account management"));

        logoutButton = new JButton("Log out");
        logoutButton.setBackground(Color.LIGHT_GRAY);
        logoutButton.setForeground(Color.BLACK);

        // Action listener to switch back to LogInPanel
        logoutButton.addActionListener(e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel")));

        accountManagementPanel.add(logoutButton);

        // Combine Panels
        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(new BoxLayout(managementPanel, BoxLayout.Y_AXIS));
        managementPanel.add(tradingManagementPanel);
        managementPanel.add(Box.createVerticalStrut(10));
        managementPanel.add(accountManagementPanel);

        add(managementPanel, BorderLayout.CENTER);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent) {
            System.out.println("DashboardPanel received a SwitchPanelEvent.");
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        // DashboardPanel only supports SWITCH_PANEL events
        return EnumSet.of(EventType.SWITCH_PANEL);
    }
}