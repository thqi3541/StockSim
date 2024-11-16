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
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.EnumSet;

public class TransactionHistoryPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 20;
    private static final int PANEL_MIN_WIDTH = 400;
    private static final int PANEL_MIN_HEIGHT = 300;
    private static final int PANEL_PREF_WIDTH = 600;
    private static final int PANEL_PREF_HEIGHT = 400;
    private static final int TABLE_HEIGHT = 200;
    private static final int ROW_HEIGHT = 30;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 30;

    // Font Constants
    private static final String FONT_FAMILY = "Arial";
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

    private final DefaultTableModel tableModel;
    private final JTable historyTable;

    public TransactionHistoryPanel() {
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
        historyTable = createHistoryTable();
        JScrollPane tableScrollPane = new JScrollPane(historyTable);
        tableScrollPane.setPreferredSize(new Dimension(PANEL_PREF_WIDTH, TABLE_HEIGHT));
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void setupPanelLayout() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(PANEL_MIN_WIDTH, PANEL_MIN_HEIGHT));
        setPreferredSize(new Dimension(PANEL_PREF_WIDTH, PANEL_PREF_HEIGHT));
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));

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

    private JTable createHistoryTable() {
        JTable table = new JTable(tableModel);

        // Table properties
        table.setFillsViewportHeight(true);
        table.setRowHeight(ROW_HEIGHT);
        table.setFont(new Font(FONT_FAMILY, Font.PLAIN, TABLE_FONT_SIZE));

        // Header properties
        table.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, TABLE_FONT_SIZE));
        table.getTableHeader().setForeground(Color.GRAY);

        // Column properties
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        return table;
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