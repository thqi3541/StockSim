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

public class TradeSimulationPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int MAIN_PADDING = 20;
    private static final int SECTION_PADDING = 10;
    private static final Insets HEADER_INSETS = new Insets(20, 20, 20, 20);
    private static final Insets PANEL_SPACING = new Insets(10, 0, 10, 0);

    // Component Sizes
    private static final Dimension BACK_BUTTON_SIZE = new Dimension(120, 30);
    private static final Dimension ORDER_PANEL_SIZE = new Dimension(200, 0);
    private static final Dimension VERTICAL_GAP = new Dimension(0, 10);

    // Font Constants
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);

    // Text Constants
    private static final String TITLE_TEXT = "Trade Simulation";
    private static final String BACK_BUTTON_TEXT = "Back to Home";

    public TradeSimulationPanel() {
        ViewManager.Instance().registerComponent(this);
        setupMainPanel();

        // Add components in order
        add(createHeaderPanel());
        add(createUpperPanel());
        add(createLowerPanel());
    }

    private void setupMainPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(
                MAIN_PADDING, MAIN_PADDING, MAIN_PADDING, MAIN_PADDING));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(
                HEADER_INSETS.top, HEADER_INSETS.left,
                HEADER_INSETS.bottom, HEADER_INSETS.right));

        // Title
        JLabel titleLabel = new JLabel(TITLE_TEXT);
        titleLabel.setFont(TITLE_FONT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back button
        headerPanel.add(createBackButton(), BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createBackButton() {
        ButtonComponent backButton = new ButtonComponent(BACK_BUTTON_TEXT);
        backButton.setPreferredSize(BACK_BUTTON_SIZE);
        backButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel")));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private JPanel createUpperPanel() {
        JPanel upperPanel = new JPanel(new GridBagLayout());
        upperPanel.setBorder(BorderFactory.createEmptyBorder(
                PANEL_SPACING.top, 0, PANEL_SPACING.bottom, 0));

        JPanel searchAndOrderPanel = createSearchAndOrderPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        upperPanel.add(searchAndOrderPanel, gbc);
        return upperPanel;
    }

    private JPanel createSearchAndOrderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Market Search Panel
        MarketSearchPanel marketSearchPanel = new MarketSearchPanel();
        marketSearchPanel.setBorder(BorderFactory.createEmptyBorder(
                SECTION_PADDING, SECTION_PADDING, SECTION_PADDING, SECTION_PADDING));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 4.0;
        gbc.insets = new Insets(0, 0, 0, SECTION_PADDING);
        panel.add(marketSearchPanel, gbc);

        // Order Entry Panel
        OrderEntryPanel orderEntryPanel = new OrderEntryPanel();
        orderEntryPanel.setPreferredSize(ORDER_PANEL_SIZE);
        orderEntryPanel.setBorder(BorderFactory.createEmptyBorder(
                SECTION_PADDING, SECTION_PADDING, SECTION_PADDING, SECTION_PADDING));

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, SECTION_PADDING, 0, 0);
        panel.add(orderEntryPanel, gbc);

        return panel;
    }

    private JPanel createLowerPanel() {
        JPanel assetAndPortfolioPanel = new JPanel();
        assetAndPortfolioPanel.setLayout(new BoxLayout(
                assetAndPortfolioPanel, BoxLayout.Y_AXIS));
        assetAndPortfolioPanel.setBorder(BorderFactory.createEmptyBorder(
                SECTION_PADDING, 0, 0, 0));

        // Add Asset Panel
        assetAndPortfolioPanel.add(new AssetPanel());
        assetAndPortfolioPanel.add(Box.createRigidArea(VERTICAL_GAP));

        // Add Portfolio Panel
        assetAndPortfolioPanel.add(new PortfolioPanel());

        return assetAndPortfolioPanel;
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent) {
            System.out.println("TradeSimulationPanel received a SwitchPanelEvent.");
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.SWITCH_PANEL);
    }
}