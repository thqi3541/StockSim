package view.panels;

import utility.FontManager;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.SwitchPanelEvent;
import view.view_events.UpdateAssetEvent;
import view.view_events.UpdateUsernameEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel implements IComponent {
    private static final int PADDING = 20;
    private static final String CURRENCY_FORMAT = "$%.2f";

    private final JLabel welcomeLabel;
    private final JLabel assetLabel;
    private final ButtonComponent tradeButton;
    private final ButtonComponent historyButton;
    private final ButtonComponent logoutButton;

    public DashboardPanel() {
        welcomeLabel = new JLabel("Welcome back, Guest!");
        assetLabel = new JLabel(formatAssetValue(0.0, 0.0));
        tradeButton = new ButtonComponent("Trade");
        historyButton = new ButtonComponent("Transaction History");
        logoutButton = new ButtonComponent("Log out");

        ViewManager.Instance().registerComponent(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        add(createHeaderSection());
        add(Box.createRigidArea(new Dimension(0, PADDING)));
        add(createDashboardSection());
        add(Box.createRigidArea(new Dimension(0, PADDING)));
        add(createAccountSection());
        add(Box.createRigidArea(new Dimension(0, PADDING)));
        add(createFooterSection());

        setupButtonActions();
    }

    private JPanel createHeaderSection() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);

        FontManager.Instance().useBold(welcomeLabel, 24f);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        FontManager.Instance().useRegular(assetLabel, 18f);
        assetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(welcomeLabel);
        header.add(Box.createRigidArea(new Dimension(0, 10)));
        header.add(assetLabel);

        return header;
    }

    private JPanel createDashboardSection() {
        JPanel dashboardSection = new JPanel();
        dashboardSection.setLayout(new BoxLayout(dashboardSection, BoxLayout.Y_AXIS));
        dashboardSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboardSection.setBorder(BorderFactory.createTitledBorder("Dashboard"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        FontManager.Instance().useRegular(tradeButton, 14f);
        FontManager.Instance().useRegular(historyButton, 14f);
        buttonPanel.add(tradeButton);
        buttonPanel.add(historyButton);

        dashboardSection.add(buttonPanel);
        return dashboardSection;
    }

    private JPanel createAccountSection() {
        JPanel accountSection = new JPanel();
        accountSection.setLayout(new BoxLayout(accountSection, BoxLayout.Y_AXIS));
        accountSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountSection.setBorder(BorderFactory.createTitledBorder("Account"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        FontManager.Instance().useRegular(logoutButton, 14f);
        buttonPanel.add(logoutButton);

        accountSection.add(buttonPanel);
        return accountSection;
    }

    private JPanel createFooterSection() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footerLabel = new JLabel("Made with \u2764 by Group 184.");
        FontManager.Instance().useRegular(footerLabel, 14f);
        footer.add(footerLabel);

        return footer;
    }

    private String formatAssetValue(double cash, double portfolio) {
        return String.format("You have %s in cash and %s in portfolio.",
                String.format(CURRENCY_FORMAT, cash),
                String.format(CURRENCY_FORMAT, portfolio));
    }

    private void setupButtonActions() {
        tradeButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TradeSimulationPanel"))
        );

        historyButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("TransactionHistoryPanel"))
        );

        logoutButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel"))
        );
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateUsernameEvent userEvent) {
            updateDashboardUserName(userEvent.getUsername());
        } else if (event instanceof UpdateAssetEvent assetEvent) {
            updateDashboardValue(assetEvent.getBalance(), assetEvent.getPortfolio().getTotalValue());
        }
    }

    private void updateDashboardUserName(String username) {
        welcomeLabel.setText("Welcome back, " + username + "!");
    }

    private void updateDashboardValue(double cash, double position) {
        assetLabel.setText(formatAssetValue(cash, position));
    }
}
