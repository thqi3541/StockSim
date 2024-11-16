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
    private static final String CASH_LABEL = "Cash: ";
    private static final String STOCK_LABEL = "Stock: ";

    // Format and style constants
    private static final String DEFAULT_VALUE = "$0.00";
    private static final String CURRENCY_FORMAT = "$%,.2f";
    private static final String FONT_FAMILY = "Arial";
    private static final int TOTAL_ASSETS_FONT_SIZE = 16;
    private static final int DETAIL_FONT_SIZE = 14;

    // Layout constants
    private static final int BORDER_PADDING = 10;
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 150;

    // Component references
    private final JLabel totalAssetsLabel;
    private final JLabel cashLabel;
    private final JLabel stockLabel;

    public AssetPanel() {
        ViewManager.Instance().registerComponent(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));

        // Total Assets label setup
        totalAssetsLabel = new JLabel(TOTAL_ASSETS_LABEL + DEFAULT_VALUE);
        totalAssetsLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, TOTAL_ASSETS_FONT_SIZE));
        totalAssetsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(totalAssetsLabel, BorderLayout.NORTH);

        // Separator for visual structure
        add(new JSeparator(), BorderLayout.CENTER);

        // Bottom panel for Cash and Stock labels
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Cash label
        cashLabel = new JLabel(CASH_LABEL + DEFAULT_VALUE);
        cashLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, DETAIL_FONT_SIZE));
        cashLabel.setForeground(Color.GRAY);

        // Stock label
        stockLabel = new JLabel(STOCK_LABEL + DEFAULT_VALUE);
        stockLabel.setFont(new Font(FONT_FAMILY, Font.PLAIN, DETAIL_FONT_SIZE));
        stockLabel.setForeground(Color.GRAY);
        stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        bottomPanel.add(cashLabel, BorderLayout.WEST);
        bottomPanel.add(stockLabel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent updateEvent) {
            Portfolio portfolio = updateEvent.getPortfolio();
            double balance = updateEvent.getBalance();
            double stockValue = portfolio.getTotalValue();
            double totalAssets = stockValue + balance;

            totalAssetsLabel.setText(TOTAL_ASSETS_LABEL + String.format(CURRENCY_FORMAT, totalAssets));
            cashLabel.setText(CASH_LABEL + String.format(CURRENCY_FORMAT, balance));
            stockLabel.setText(STOCK_LABEL + String.format(CURRENCY_FORMAT, stockValue));
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.UPDATE_ASSET);
    }
}