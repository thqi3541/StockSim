package view.GUI;

import javax.swing.*;
import java.awt.*;

public class OrderEntryPanel extends JPanel {
    private final JTextField tickerField;
    private final JTextField quantityField;
    private final JButton buyButton;
    private final JButton sellButton;

    public OrderEntryPanel() {
        setLayout(new GridLayout(3, 2));
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

