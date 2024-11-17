package view.panels;

import entity.Stock;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.components.InputComponent;
import view.view_events.EventType;
import view.view_events.UpdateStockEvent;
import view.view_events.UpdateUsernameEvent;
import view.view_events.ViewEvent;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EnumSet;
import java.util.Objects;

public class MarketSearchPanel extends JPanel implements IComponent {
    private final InputComponent searchField;
    private final ButtonComponent searchButton;
    private final JLabel titleLabel;
    private final JTable stockTable;

    public MarketSearchPanel() {
        ViewManager.Instance().registerComponent(this);

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(600, 400));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with Title and Search Field/Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        titleLabel = new JLabel("Market Search");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Search Field and Button in a FlowLayout
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new InputComponent(10);
        searchButton = new ButtonComponent("Search");

        // Add listener to search button (placeholder for functionality)
        searchButton.addActionListener(e -> {
            // Logic for searching stocks would go here
            System.out.println("Search button clicked for ticker: " + searchField.getText());
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Stock Table Section
        JPanel bodyPanel = new JPanel(new BorderLayout());
        stockTable = new JTable(getDefaultTableModel());
        JScrollPane tableScrollPane = new JScrollPane(stockTable);

        bodyPanel.add(tableScrollPane, BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Market Search Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        MarketSearchPanel marketSearchPanel = new MarketSearchPanel();
        frame.add(marketSearchPanel);
        frame.setVisible(true);
    }

    // Table model setup for demonstration purposes
    private DefaultTableModel getDefaultTableModel() {
        String[] columnNames = {"Ticker", "Company Name", "Price"};
        Object[][] data = {
                {"AAPL", "Apple Inc.", "Fruit Retail", "150.00"},
                {"GOOG", "Alphabet Inc.", "Spelling Education", "2800.00"},
                {"TSLA", "Tesla Inc.", "Electricity", "900.00"}
        };
        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table cells non-editable
            }
        };
    }

    private void updateStockTable(List<Stock> stocks) {
        // Define columns (same as in getDefaultTableModel)
        String[] columnNames = {"Ticker", "Company Name", "Industry", "Price"};

        // Create a 2D array to hold the data for the table
        Object[][] data = new Object[stocks.size()][4];
        for (int i = 0; i < stocks.size(); i++) {
            data[i][0] = stocks.get(i).getTicker();
            data[i][1] = stocks.get(i).getCompany();
            data[i][2] = stocks.get(i).getIndustry();
            data[i][3] = stocks.get(i).getPrice();
        }

        // Update the table model with the new data
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Make table cells non-editable
            }
        };
        stockTable.setModel(model);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        // Set up for future event handling
        // event not used by panel
        if (Objects.requireNonNull(event) instanceof UpdateStockEvent updateStockEvent) {
            // Update the table with the new stock data
            updateStockTable(updateStockEvent.getStocks());
        }
    }
}