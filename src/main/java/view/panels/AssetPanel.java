package view.panels;

import entity.Portfolio;
import utility.ViewManager;
import view.IComponent;
import view.view_events.EventType;
import view.view_events.UpdateAssetEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class AssetPanel extends JPanel implements IComponent {
    // Label text constants
    private static final String TOTAL_ASSETS_LABEL = "Total Assets: ";
    private static final String BALANCE_LABEL = "Balance: ";
    private static final String PORTFOLIO_VALUE_LABEL = "Total Value in Portfolio: ";

    // Format and style constants
    private static final String DEFAULT_VALUE = "$0.00";
    private static final String CURRENCY_FORMAT = "$%,.2f";
    private static final String FONT_FAMILY = "Lucida Sans";
    private static final int TOTAL_ASSETS_FONT_SIZE = 16;
    private static final int DETAIL_FONT_SIZE = 14;

    // Layout constants
    private static final int BORDER_PADDING = 10;
    private static final int DETAIL_GAP = 5; // Gap between lines and separator

    // Component references
    private final JLabel totalAssetsLabel;
    private final JLabel balanceLabel;
    private final JLabel portfolioValueLabel;

    public AssetPanel() {
        ViewManager.Instance().registerComponent(this);

        // Use BoxLayout to manage vertical alignment
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));

        // Total Assets label setup
        totalAssetsLabel = new JLabel(TOTAL_ASSETS_LABEL + DEFAULT_VALUE);
        totalAssetsLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, TOTAL_ASSETS_FONT_SIZE));
        totalAssetsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(totalAssetsLabel);

        // Separator for visual structure
        add(Box.createVerticalStrut(DETAIL_GAP)); // Gap before separator
        add(new JSeparator());
        add(Box.createVerticalStrut(DETAIL_GAP)); // Gap after separator

        // Bottom panel for Cash and Stock labels
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cash label
        balanceLabel = new JLabel(BALANCE_LABEL + DEFAULT_VALUE);
        balanceLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, DETAIL_FONT_SIZE));
        balanceLabel.setForeground(Color.GRAY);

        // Stock label
        portfolioValueLabel = new JLabel(PORTFOLIO_VALUE_LABEL + DEFAULT_VALUE);
        portfolioValueLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, DETAIL_FONT_SIZE));
        portfolioValueLabel.setForeground(Color.GRAY);

        // Add labels to the bottom panel
        bottomPanel.add(balanceLabel);
        bottomPanel.add(Box.createHorizontalGlue()); // Push the stock label to the right
        bottomPanel.add(portfolioValueLabel);

        add(bottomPanel);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent updateEvent) {
            Portfolio portfolio = updateEvent.getPortfolio();
            double balance = updateEvent.getBalance();
            double portfolioTotalValue = portfolio.getTotalValue();
            double totalAssets = portfolioTotalValue + balance;

            totalAssetsLabel.setText(TOTAL_ASSETS_LABEL + String.format(CURRENCY_FORMAT, totalAssets));
            balanceLabel.setText(BALANCE_LABEL + String.format(CURRENCY_FORMAT, balance));
            portfolioValueLabel.setText(PORTFOLIO_VALUE_LABEL + String.format(CURRENCY_FORMAT, portfolioTotalValue));
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.UPDATE_ASSET);
    }
}
