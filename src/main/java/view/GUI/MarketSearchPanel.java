package view.GUI;

import javax.swing.*;
import java.awt.*;

public class MarketSearchPanel extends JPanel {
    private final JTextField searchField;
    private final JButton searchButton;

    public MarketSearchPanel() {
        setLayout(new GridLayout(1, 2));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        add(searchField);
        add(searchButton);
    }
}


