package view.panels;

import view.FontManager;
import view.IComponent;
import view.ViewManager;
import view.components.ButtonComponent;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;

public class TradeSimulationPanel extends JPanel implements IComponent {

    private static final int PADDING = 20;
    private static final double MAIN_PANEL_RATIO = 0.8;
    private static final double SIDE_PANEL_RATIO = 0.2;

    public TradeSimulationPanel() {
        ViewManager.Instance().registerComponent(this);

        // Set up main panel with vertical BoxLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING,
                                                  PADDING));

        // Add header (fill width)
        JPanel headerPanel = createHeaderPanel();
        headerPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE,
                              headerPanel.getPreferredSize().height));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(headerPanel);

        // Add gap (20)
        add(Box.createRigidArea(new Dimension(0, PADDING)));

        // Add trading section (fixed height, fill width)
        JPanel tradingPanel = createTradingSection();
        tradingPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE,
                              tradingPanel.getPreferredSize().height));
        tradingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(tradingPanel);

        // Add gap (20)
        add(Box.createRigidArea(new Dimension(0, PADDING)));

        // Add portfolio section (automatic height, fill width)
        JPanel portfolioPanel = createAccountSection();
        portfolioPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        portfolioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(portfolioPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Title on the left
        JLabel titleLabel = new JLabel("Trade Simulation");
        FontManager.Instance().useBold(titleLabel, 24f);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back button on the right
        ButtonComponent backButton = new ButtonComponent("Back to Home");
        backButton.addActionListener(
                e -> ViewManager.Instance().broadcastEvent(
                        new SwitchPanelEvent("DashboardPanel")));
        headerPanel.add(backButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTwoColumnPanel(JPanel leftPanel, JPanel rightPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridheight = 1;
        gbc.gridy = 0;

        // Left panel (75% width)
        gbc.gridx = 0;
        gbc.weightx = MAIN_PANEL_RATIO;
        gbc.insets = new Insets(0, 0, 0, PADDING / 2);
        panel.add(leftPanel, gbc);

        // Right panel (25% width)
        gbc.gridx = 1;
        gbc.weightx = SIDE_PANEL_RATIO;
        gbc.insets = new Insets(0, PADDING / 2, 0, 0);
        panel.add(rightPanel, gbc);

        // Force minimum sizes to help maintain proportions
        leftPanel.setMinimumSize(new Dimension(0, 0));
        rightPanel.setMinimumSize(new Dimension(0, 0));

        return panel;
    }

    private JPanel createTradingSection() {
        MarketSearchPanel marketSearchPanel = new MarketSearchPanel();
        OrderEntryPanel orderEntryPanel = new OrderEntryPanel();
        return createTwoColumnPanel(marketSearchPanel, orderEntryPanel);
    }

    private JPanel createAccountSection() {
        PortfolioPanel portfolioTablePanel = new PortfolioPanel();
        AssetPanel assetPanel = new AssetPanel();
        return createTwoColumnPanel(portfolioTablePanel, assetPanel);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
    }
}
