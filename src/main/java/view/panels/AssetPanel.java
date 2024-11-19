package view.panels;

import utility.FontManager;
import utility.ViewManager;
import view.IComponent;
import view.view_events.UpdateAssetEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;

public class AssetPanel extends JPanel implements IComponent {
    private static final String TITLE = "Assets Overview";
    private static final int HEADER_HEIGHT = 40;
    private static final int PADDING = 20;
    private static final int INNER_GAP = 10;

    private final JLabel cashLabel;
    private final JLabel portfolioLabel;
    private final JLabel totalLabel;

    public AssetPanel() {
        ViewManager.Instance().registerComponent(this);

        // Initialize labels
        totalLabel = new JLabel("Total Assets: $0.00");
        cashLabel = new JLabel("Cash: $0.00");
        portfolioLabel = new JLabel("Portfolio Value: $0.00");

        setupPanel();
    }

    private void setupPanel() {
        // Use GridBagLayout for better size control
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        // Add header
        JPanel headerPanel = createHeaderPanel();
        gbc.gridy = 0;
        add(headerPanel, gbc);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints contentGbc = new GridBagConstraints();
        contentGbc.gridx = 0;
        contentGbc.fill = GridBagConstraints.HORIZONTAL;
        contentGbc.weightx = 1.0;
        contentGbc.anchor = GridBagConstraints.WEST;

        // Add total assets section
        FontManager.Instance().useRegular(totalLabel, 14f);
        contentGbc.gridy = 0;
        contentGbc.insets = new Insets(0, 0, INNER_GAP, 0);
        contentPanel.add(totalLabel, contentGbc);

        // Add separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        contentGbc.gridy = 1;
        contentGbc.insets = new Insets(0, 0, INNER_GAP, 0);
        contentPanel.add(separator, contentGbc);

        // Add details section
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints detailsGbc = new GridBagConstraints();
        detailsGbc.gridx = 0;
        detailsGbc.fill = GridBagConstraints.HORIZONTAL;
        detailsGbc.weightx = 1.0;
        detailsGbc.anchor = GridBagConstraints.WEST;

        // Style and add detail labels
        int labelIndex = 0;
        for (JLabel label : new JLabel[]{cashLabel, portfolioLabel}) {
            FontManager.Instance().useRegular(label, 14f);
            detailsGbc.gridy = labelIndex++;
            detailsGbc.insets = new Insets(0, 0, 5, 0);
            detailsPanel.add(label, detailsGbc);
        }

        contentGbc.gridy = 2;
        contentGbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(detailsPanel, contentGbc);

        // Add content panel to main panel
        gbc.gridy = 1;
        gbc.insets = new Insets(PADDING, 0, 0, 0);
        add(contentPanel, gbc);

        // Add bottom padding
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel(TITLE);
        FontManager.Instance().useBold(titleLabel, 18f);
        headerPanel.add(titleLabel, gbc);

        return headerPanel;
    }

    private void updateValues(double cash, double portfolioValue) {
        totalLabel.setText(String.format("Total Assets: $%.2f", cash + portfolioValue));
        cashLabel.setText(String.format("Cash: $%.2f", cash));
        portfolioLabel.setText(String.format("Portfolio Value: $%.2f", portfolioValue));
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent assetEvent) {
            updateValues(assetEvent.getBalance(), assetEvent.getPortfolio().getTotalValue());
        }
    }
}
