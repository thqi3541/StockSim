package view.panels;

import entity.Portfolio;
import entity.UserStock;
import utility.ViewManager;
import view.IComponent;
import view.view_events.UpdateAssetEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PortfolioPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 10;
    private static final int PANEL_HEIGHT = 300;
    private static final int TABLE_HEIGHT = 200;
    private static final int ROW_HEIGHT = 30;
    private static final int HEADER_HEIGHT = 30;

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
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
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
        tableScrollPane.setPreferredSize(new Dimension(0, TABLE_HEIGHT));
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(tableScrollPane, BorderLayout.CENTER);

        // Add resize listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustColumnWidths();
            }
        });

        // Initial column width adjustment
        adjustColumnWidths();
    }

    private void setupPanelLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header properties
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font(FONT_FAMILY, Font.BOLD, TABLE_FONT_SIZE));
        header.setForeground(Color.GRAY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, HEADER_HEIGHT));
        header.setReorderingAllowed(false);

        return table;
    }

    private void adjustColumnWidths() {
        int availableWidth = getWidth() - (2 * BORDER_PADDING);
        if (availableWidth <= 0) return;

        // Column proportions (must sum to 1.0)
        double[] columnProportions = {0.15, 0.17, 0.13, 0.17, 0.19, 0.19};

        for (int i = 0; i < portfolioTable.getColumnCount(); i++) {
            TableColumn column = portfolioTable.getColumnModel().getColumn(i);
            int width = (int) (availableWidth * columnProportions[i]);
            column.setPreferredWidth(width);
            column.setMinWidth(width);
            column.setMaxWidth(width);
        }
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateAssetEvent assetEvent) {
            Portfolio portfolio = assetEvent.getPortfolio();
            tableModel.setRowCount(0);

            portfolio.getAllStocks().forEach(userStock -> {
                Object[] rowData = createRowData(userStock);
                tableModel.addRow(rowData);
            });

            adjustColumnWidths();
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
