package view.panels;

import entity.Transaction;
import entity.TransactionHistory;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.EventType;
import view.view_events.SwitchPanelEvent;
import view.view_events.UpdateTransactionHistoryEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

public class TransactionHistoryPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int MAIN_PADDING = 20;
    private static final int HEADER_BOTTOM_SPACING = 10;
    private static final int ROW_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 30;

    // Font Constants
    private static final String FONT_FAMILY = "Lucida Sans";
    private static final int TITLE_FONT_SIZE = 28;
    private static final int TABLE_FONT_SIZE = 14;

    // Format Constants
    private static final String CURRENCY_FORMAT = "%.2f";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    // Column Constants
    private static final String[] COLUMN_NAMES = {
            "Date",
            "Ticker",
            "Company Name",
            "Industry",
            "Action",
            "Price",
            "Quantity",
            "Total Price"
    };

    // Column Width Proportions (total = 1.0)
    private static final double[] COLUMN_PROPORTIONS = {
            0.15,  // Date
            0.08,  // Ticker
            0.25,  // Company Name
            0.20,  // Industry
            0.08,  // Action
            0.08,  // Price
            0.08,  // Quantity
            0.08   // Total Price
    };

    // Components
    private final DefaultTableModel tableModel;
    private final JTable historyTable;

    public TransactionHistoryPanel() {
        // Initialize table model and table first
        tableModel = createTableModel();
        historyTable = createHistoryTable();

        // Register component
        ViewManager.Instance().registerComponent(this);

        // Set up main panel layout
        setupMainPanel();

        // Add components
        add(createHeaderWithGapPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        // Add resize listener
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustColumnWidths();
            }
        });
    }

    private void setupMainPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(
                MAIN_PADDING, MAIN_PADDING, MAIN_PADDING, MAIN_PADDING));
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(new Object[][]{}, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel createHeaderWithGapPanel() {
        // Wrapper panel for header and gap
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        wrapperPanel.add(headerPanel);

        // Gap
        wrapperPanel.add(Box.createRigidArea(new Dimension(0, HEADER_BOTTOM_SPACING)));

        return wrapperPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font(FONT_FAMILY, Font.BOLD, TITLE_FONT_SIZE));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back button
        ButtonComponent backButton = new ButtonComponent("Back to Home");
        backButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        backButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel"))
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JScrollPane createTablePanel() {
        JScrollPane tableScrollPane = new JScrollPane(historyTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return tableScrollPane;
    }

    private JTable createHistoryTable() {
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(ROW_HEIGHT);
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, TABLE_FONT_SIZE));
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, TABLE_FONT_SIZE));
        table.getTableHeader().setForeground(Color.GRAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    private void adjustColumnWidths() {
        int availableWidth = getWidth() - (2 * MAIN_PADDING);
        if (availableWidth <= 0) return;

        for (int col = 0; col < historyTable.getColumnCount(); col++) {
            TableColumn column = historyTable.getColumnModel().getColumn(col);
            int columnWidth = (int) (COLUMN_PROPORTIONS[col] * availableWidth);
            column.setPreferredWidth(columnWidth);
        }
    }

    private Object[] createRowData(Transaction transaction) {
        String ticker = transaction.getTicker();
        String formattedDate = DATE_FORMAT.format(transaction.getTimestamp());
        String company = "Unknown Company";
        String industry = "Unknown Industry";
        String action = transaction.getType();
        double price = transaction.getPrice();
        int quantity = transaction.getQuantity();
        double totalPrice = price * quantity;

        return new Object[]{
                formattedDate,
                ticker,
                company,
                industry,
                action,
                String.format("$" + CURRENCY_FORMAT, price),
                quantity,
                String.format("$" + CURRENCY_FORMAT, totalPrice)
        };
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateTransactionHistoryEvent updateEvent) {
            TransactionHistory transactionHistory = updateEvent.getTransactionHistory();
            tableModel.setRowCount(0);

            transactionHistory.getAllTransactions().forEach(transaction -> {
                Object[] rowData = createRowData(transaction);
                tableModel.addRow(rowData);
            });
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.UPDATE_TRANSACTION_HISTORY);
    }
}
