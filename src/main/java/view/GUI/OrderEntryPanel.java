package view.GUI;

import javax.swing.*;
import java.awt.*;

public class OrderEntryPanel extends JPanel {
    private JTextField tickerField, quantityField;
    private JButton buyButton, sellButton;

    public OrderEntryPanel() {
        setLayout(new GridLayout(3, 2));  // 三行两列的网格布局
        add(new JLabel("Ticker:"));
        tickerField = new JTextField();
        add(tickerField);
        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        buyButton = new JButton("Buy");
        sellButton = new JButton("Sell");
        add(buyButton);
        add(sellButton);
    }
}

