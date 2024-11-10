package view.gui_components;

import javax.swing.*;
import java.awt.*;

public class MarketSearchPanel extends JPanel {
    private JTextField searchField;
    private JButton searchButton;

    public MarketSearchPanel() {
        setLayout(new GridLayout(1, 2));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        add(searchField);
        add(searchButton);
    }
}

