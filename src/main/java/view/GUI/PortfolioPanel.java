package view.GUI;

import javax.swing.*;
import java.awt.*;

/**public class PortfolioPanel extends JPanel {
    private JTable table;
    private JScrollPane scrollPane;

    public PortfolioPanel() {
        setLayout(new BorderLayout());
        String[] columnNames = {"Ticker", "Average Cost", "Quantity", "Market Price", "Profit/Loss"};
        Object[][] data = {};  // 填入数据

        table = new JTable(data, columnNames);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
    */

public class PortfolioPanel extends JPanel {
    private JTable table;

    public PortfolioPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnNames = {"Ticker", "Company Name", "Average Cost", "Quantity", "Market Price", "Profit / Share", "Total Profit"};
        Object[][] data = {
                {"NVDA", "NVIDIA Corporation", 138.27, 2, 158.03, 19.76, 39.52},
                {"TSLA", "Tesla, Inc.", 110.23, 12, 101.85, -8.38, -100.56}
        };

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
