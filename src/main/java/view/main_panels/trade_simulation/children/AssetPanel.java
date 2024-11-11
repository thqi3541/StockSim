package view.main_panels.trade_simulation.children;

import org.json.JSONObject;
import view.IComponent;
import view.view_event.EventType;
import view.view_event.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class AssetPanel extends JPanel implements IComponent {
    private final JLabel totalAssetsLabel;
    private final JLabel cashLabel;
    private final JLabel stockLabel;

    public AssetPanel() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("totalAssets", 5000000.00);
        jsonData.put("cash", 100000.00);
        jsonData.put("stock", 4900000.00);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalAssetsLabel = new JLabel("Total Assets: $" + String.format("%,.2f", jsonData.getDouble("totalAssets")));
        totalAssetsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalAssetsLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(totalAssetsLabel, BorderLayout.NORTH);

        JSeparator separator = new JSeparator();
        add(separator, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        cashLabel = new JLabel("Cash: $" + String.format("%,.2f", jsonData.getDouble("cash")));
        cashLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cashLabel.setForeground(Color.GRAY);

        stockLabel = new JLabel("Stock: $" + String.format("%,.2f", jsonData.getDouble("stock")));
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        stockLabel.setForeground(Color.GRAY);
        stockLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        bottomPanel.add(cashLabel, BorderLayout.WEST);
        bottomPanel.add(stockLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setPreferredSize(null);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        // The logic for handling UpdatePortfolioEvent will be implemented later.
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        // AssetPanel supports UPDATE_PORTFOLIO events
        return EnumSet.of(EventType.UPDATE_ASSET);
    }
}