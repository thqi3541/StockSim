package view.GUI;

import javax.swing.*;
import java.awt.*;

public class TotalAssetsPanel extends JPanel {
    private final JLabel totalAssetsLabel;
    private final JLabel cashLabel;
    private final JLabel stockLabel;

    public TotalAssetsPanel() {

        // TODO: BoxLayout改成BorderLayout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalAssetsLabel = new JLabel("Total Assets: $XXX,XXX,XXX");
        totalAssetsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalAssetsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 5));
        cashLabel = new JLabel("Cash: $XX,XXX");
        stockLabel = new JLabel("Stock: $XX,XXX");

        setLabelsStyle(cashLabel);
        setLabelsStyle(stockLabel);

        lowerPanel.add(cashLabel);
        lowerPanel.add(stockLabel);

        add(totalAssetsLabel);
        add(lowerPanel);
    }

    private void setLabelsStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.PLAIN, 14));
    }
}