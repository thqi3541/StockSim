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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.EnumSet;

public class PortfolioPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 10; // Padding around the panel content
    private static final int PANEL_HEIGHT = 300; // Height of the panel
    private static final int TABLE_HEIGHT = 200; // Height of the table
    private static final int ROW_HEIGHT = 30;    // Height of each row in the table
    private static final int HEADER_HEIGHT = 30; // Height of the table header

    // Font Constants
    private static final String FONT_FAMILY = "Lucida Sans";
    private static final int TITLE_FONT_SIZE = 16;
    private static final int TABLE_FONT_SIZE = 14;

    // Format Constants
    private static final String CURRENCY_FORMAT = "%.2f";
    private static final String NEGATIVE_PROFIT_FORMAT = "▼ %.2f";
    private static final String POSITIVE_PROFIT_FORMAT = "▲ %.2f";

    // Column Constants
    private static final String[] COLUMN_NAMES = {
            "Ticker",
            "Average Cost",
            "Quantity",
            "Market Price",
            "Profit / Share",
            "Total Profit"
    };

    private final JTable portfolioTable;
    private final DefaultTableModel tableModel;
    private JLabel titleLabel;

    public PortfolioPanel() {
        ViewManager.Instance().registerComponent(this);
        setupPanelLayout();

        // Initialize table model
        tableModel = new DefaultTableModel(new Object[][]{}, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Setup header panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Setup table
        portfolioTable = createPortfolioTable();
        JScrollPane tableScrollPane = new JScrollPane(portfolioTable);
        tableScrollPane.setPreferredSize(new Dimension(0, TABLE_HEIGHT)); // Let width adjust to parent
        add(tableScrollPane, BorderLayout.CENTER);

        // Add resize listener to adjust column widths dynamically
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustColumnWidths(portfolioTable);
            }
        });

        // Initial column width adjustment
        adjustColumnWidths(portfolioTable);
    }

    private void setupPanelLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, PANEL_HEIGHT)); // Let width adjust to parent
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(
                0, 0, BORDER_PADDING, 0));

        titleLabel = new JLabel("Portfolio Overview");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        return headerPanel;
    }

    private JTable createPortfolioTable() {
        JTable table = new JTable(tableModel);

        // Table properties
        table.setFillsViewportHeight(true);
        table.setRowHeight(ROW_HEIGHT);
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, TABLE_FONT_SIZE));

        // Header properties
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font(FONT_FAMILY, Font.BOLD, TABLE_FONT_SIZE));
        header.setForeground(Color.GRAY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, HEADER_HEIGHT));

        // Column properties
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable automatic resizing

        return table;
    }

    private void adjustColumnWidths(JTable table) {
        int parentWidth = this.getWidth(); // Get the parent container's width

        // Proportions for each column
        double[] columnProportions = {0.10, 0.15, 0.10, 0.15, 0.25, 0.25}; // 10%, 15%, etc.
        for (int col = 0; col < table.getColumnCount(); col++) {
            int columnWidth = (int) (columnProportions[col] * parentWidth);
            TableColumn column = table.getColumnModel().getColumn(col);
            column.setPreferredWidth(columnWidth);
        }
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent updateEvent) {
            Portfolio portfolio = updateEvent.getPortfolio();
            tableModel.setRowCount(0);

            portfolio.getAllStocks().forEach(userStock -> {
                Object[] rowData = createRowData(userStock);
                tableModel.addRow(rowData);
            });

            // Adjust column widths after updating the data
            adjustColumnWidths(portfolioTable);
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
                String.format("$" + CURRENCY_FORMAT, avgCost),
                quantity,
                String.format("$" + CURRENCY_FORMAT, marketPrice),
                formatProfit(profitPerShare),
                formatProfit(totalProfit)
        };
    }

    private String formatProfit(double profit) {
        if (profit < 0) {
            return String.format(NEGATIVE_PROFIT_FORMAT, Math.abs(profit));
        }
        return String.format(POSITIVE_PROFIT_FORMAT, profit);
    }
}
