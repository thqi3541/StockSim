package view.panels;

import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;

public class TradeSimulationPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int MAIN_PADDING = 20;
    private static final int SECTION_SPACING = 10;

    // Component Sizes
    private static final Dimension VERTICAL_GAP = new Dimension(0, SECTION_SPACING);

    // Font Constants
    private static final Font TITLE_FONT = new Font("Lucida Sans", Font.BOLD, 28);

    // Text Constants
    private static final String TITLE_TEXT = "Trade Simulation";
    private static final String BACK_BUTTON_TEXT = "Back to Home";

    public TradeSimulationPanel() {
        ViewManager.Instance().registerComponent(this);
        setupMainPanel();

        // Add components in order
        add(createHeaderPanel());
        add(Box.createRigidArea(VERTICAL_GAP));
        add(createUpperPanel());
        add(Box.createRigidArea(VERTICAL_GAP));
        add(createLowerPanel());
    }

    private void setupMainPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(
                MAIN_PADDING, MAIN_PADDING, MAIN_PADDING, MAIN_PADDING));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add 10px internal margin

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
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel")));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private JPanel createUpperPanel() {
        JPanel upperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Market Search Panel
        MarketSearchPanel marketSearchPanel = new MarketSearchPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 3.0; // Wider proportion
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, SECTION_SPACING);
        upperPanel.add(marketSearchPanel, gbc);

        // Order Entry Panel
        OrderEntryPanel orderEntryPanel = new OrderEntryPanel();
        gbc.gridx = 1;
        gbc.weightx = 1.0; // Narrower proportion
        gbc.insets = new Insets(0, SECTION_SPACING, 0, 0);
        upperPanel.add(orderEntryPanel, gbc);

        return upperPanel;
    }

    private JPanel createLowerPanel() {
        JPanel assetAndPortfolioPanel = new JPanel(new BorderLayout()); // Use BorderLayout for full width

        // Add Asset Panel
        AssetPanel assetPanel = new AssetPanel();
        assetPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, assetPanel.getPreferredSize().height));
        assetAndPortfolioPanel.add(assetPanel, BorderLayout.NORTH);

        // Add Portfolio Panel
        PortfolioPanel portfolioPanel = new PortfolioPanel();
        portfolioPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, portfolioPanel.getPreferredSize().height));
        assetAndPortfolioPanel.add(portfolioPanel, BorderLayout.CENTER);

        return assetAndPortfolioPanel;
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
    }
}
