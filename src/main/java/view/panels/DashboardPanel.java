package view.panels;

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
    private static final Font TITLE_FONT = new Font("Lucida Sans", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Lucida Sans", Font.PLAIN, 18);
    private static final String CURRENCY_FORMAT = "$%.2f";

    private final JLabel welcomeLabel;
    private final JLabel balanceLabel;
    private final ButtonComponent tradeButton;
    private final ButtonComponent historyButton;
    private final ButtonComponent logoutButton;

    public DashboardPanel() {
        welcomeLabel = new JLabel("Welcome back, Guest");
        balanceLabel = new JLabel(formatBalanceText(0.0, 0.0));
        tradeButton = new ButtonComponent("Trade");
        historyButton = new ButtonComponent("View Transaction History");
        logoutButton = new ButtonComponent("Log out");

        ViewManager.Instance().registerComponent(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        add(createHeaderSection());
        add(Box.createRigidArea(new Dimension(0, PADDING)));
        add(createTradingSection());
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

        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        balanceLabel.setFont(SUBTITLE_FONT);
        balanceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(welcomeLabel);
        header.add(Box.createRigidArea(new Dimension(0, 10)));
        header.add(balanceLabel);

        return header;
    }

    private JPanel createTradingSection() {
        JPanel tradingPanel = new JPanel();
        tradingPanel.setLayout(new BoxLayout(tradingPanel, BoxLayout.Y_AXIS));
        tradingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tradingPanel.setBorder(BorderFactory.createTitledBorder("Trading management"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(tradeButton);
        buttonPanel.add(historyButton);

        tradingPanel.add(buttonPanel);
        return tradingPanel;
    }

    private JPanel createAccountSection() {
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        accountPanel.setBorder(BorderFactory.createTitledBorder("Account management"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(logoutButton);

        accountPanel.add(buttonPanel);
        return accountPanel;
    }

    private JPanel createFooterSection() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footerLabel = new JLabel("Thank you for using StockSim.");
        footerLabel.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        footer.add(footerLabel);

        return footer;
    }

    private String formatBalanceText(double cash, double position) {
        return String.format("You have %s in cash and %s in position.",
                String.format(CURRENCY_FORMAT, cash),
                String.format(CURRENCY_FORMAT, position));
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
        welcomeLabel.setText("Welcome back, " + username);
    }

    private void updateDashboardValue(double cash, double position) {
        balanceLabel.setText(formatBalanceText(cash, position));
    }
}
