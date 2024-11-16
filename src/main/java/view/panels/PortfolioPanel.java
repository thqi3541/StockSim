package view.panels;

import entity.Portfolio;
import entity.UserStock;
import utility.ViewManager;
import view.IComponent;
import view.view_events.EventType;
import view.view_events.UpdateAssetEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EnumSet;

public class PortfolioPanel extends JPanel implements IComponent {
    private final JLabel titleLabel;
    private final JTable portfolioTable;
    private final DefaultTableModel tableModel;

    public PortfolioPanel() {
        ViewManager.Instance().registerComponent(this);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 300));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize table model with column names
        tableModel = new DefaultTableModel(new Object[][]{}, getColumnNames()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Header Panel with Title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        titleLabel = new JLabel("Portfolio");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Portfolio Table Setup
        portfolioTable = createPortfolioTable();
        JScrollPane tableScrollPane = new JScrollPane(portfolioTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 200));
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private JTable createPortfolioTable() {
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setForeground(Color.GRAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    private String[] getColumnNames() {
        return new String[]{"Ticker", "Average Cost", "Quantity", "Market Price", "Profit / Share", "Total Profit"};
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent updateEvent) {
            // Get the portfolio from the event
            Portfolio portfolio = updateEvent.getPortfolio();

            // Clear the current table content
            tableModel.setRowCount(0);

            // Populate table with updated data from the portfolio
            portfolio.getAllStocks().forEach(userStock -> {
                Object[] rowData = createRowData(userStock);
                tableModel.addRow(rowData);
            });
        }
    }

    private Object[] createRowData(UserStock userStock) {
        String ticker = userStock.getStock().getTicker();
        double avgCost = userStock.getCost();
        int quantity = userStock.getQuantity();
        double marketPrice = userStock.getStock().getPrice();
        double profitPerShare = marketPrice - avgCost;
        double totalProfit = profitPerShare * quantity;

        return new Object[]{
                ticker,
                String.format("%.2f", avgCost),
                quantity,
                String.format("%.2f", marketPrice),
                String.format("%.2f", profitPerShare),
                String.format("%.2f", totalProfit)
        };
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        // PortfolioPanel supports UPDATE_ASSET events
        return EnumSet.of(EventType.UPDATE_ASSET);
    }
}