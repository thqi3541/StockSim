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
    private final JLabel totalAssetsLabel;
    private final JLabel cashLabel;
    private final JLabel stockLabel;

    public AssetPanel() {
        ViewManager.Instance().registerComponent(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Total Assets label setup
        totalAssetsLabel = new JLabel("Total Assets: $0.00");
        totalAssetsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAssetsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(totalAssetsLabel, BorderLayout.NORTH);

        // Separator for visual structure
        JSeparator separator = new JSeparator();
        add(separator, BorderLayout.CENTER);

        // Bottom panel for Cash and Stock labels
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Cash label
        cashLabel = new JLabel("Cash: $0.00");
        cashLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cashLabel.setForeground(Color.GRAY);

        // Stock label
        stockLabel = new JLabel("Stock: $0.00");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        stockLabel.setForeground(Color.GRAY);
        stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Adding labels to the bottom panel
        bottomPanel.add(cashLabel, BorderLayout.WEST);
        bottomPanel.add(stockLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(300, 150));
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        // Check if the event is an instance of UpdateAssetEvent
        if (event instanceof UpdateAssetEvent updateEvent) {
            // Get the portfolio and balance data
            Portfolio portfolio = updateEvent.getPortfolio();
            double balance = updateEvent.getBalance();

            // Calculate total asset value based on portfolio and cash balance
            double stockValue = portfolio.getTotalValue(); // Assume Portfolio has a method for total stock value
            double totalAssets = stockValue + balance;

            // Update labels with the new values
            totalAssetsLabel.setText("Total Assets: $" + String.format("%,.2f", totalAssets));
            cashLabel.setText("Cash: $" + String.format("%,.2f", balance));
            stockLabel.setText("Stock: $" + String.format("%,.2f", stockValue));
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        // AssetPanel supports UPDATE_ASSET events
        return EnumSet.of(EventType.UPDATE_ASSET);
    }
}