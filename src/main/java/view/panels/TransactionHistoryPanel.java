package view.panels;

import entity.Transaction;
import entity.TransactionHistory;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.SwitchPanelEvent;
import view.view_events.UpdateTransactionHistoryEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.SimpleDateFormat;

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
            "Action",
            "Price",
            "Quantity",
            "Total Price"
    };

    // Column Width Proportions (total = 1.0)
    private static final double[] COLUMN_PROPORTIONS = {
            0.20,  // Date
            0.10,  // Ticker
            0.10,  // Action
            0.10,  // Price
            0.10,  // Quantity
            0.10   // Total Price
    };
    private final JTable historyTable;
    // Components
    private DefaultTableModel tableModel;

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
        return new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
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

    private void updateTransactionTable(TransactionHistory transactionHistory) {
        if (transactionHistory == null) {
            System.err.println("No transaction history available to update.");
            return;
        }

        // 1. Create new table model with column names
        DefaultTableModel newModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        // 2. Add data rows
        for (Transaction transaction : transactionHistory.getAllTransactions()) {
            Object[] rowData = createRowData(transaction);
            newModel.addRow(rowData);
        }

        // 3. Update table model
        historyTable.setModel(newModel);
        tableModel = newModel;  // Update the reference to the new model

        // 4. Reapply column properties
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            TableColumn column = historyTable.getColumnModel().getColumn(i);
            column.setResizable(true);
        }

        // 5. Reset header properties
        historyTable.getTableHeader().setFont(new Font(FONT_FAMILY, Font.BOLD, TABLE_FONT_SIZE));
        historyTable.getTableHeader().setForeground(Color.GRAY);

        // 6. Reapply row properties
        historyTable.setRowHeight(ROW_HEIGHT);
        historyTable.setFont(new Font(FONT_FAMILY, Font.PLAIN, TABLE_FONT_SIZE));

        // 7. Reapply column widths
        adjustColumnWidths();

        // 8. Ensure visual update
        historyTable.revalidate();
        historyTable.repaint();
    }

    private Object[] createRowData(Transaction transaction) {
        String ticker = transaction.getTicker();
        String formattedDate = DATE_FORMAT.format(transaction.getTimestamp());
        String action = transaction.getType();
        double price = transaction.getPrice();
        int quantity = transaction.getQuantity();
        double totalPrice = price * quantity;

        return new Object[]{
                formattedDate,
                ticker,
                action,
                String.format("$" + CURRENCY_FORMAT, price),
                quantity,
                String.format("$" + CURRENCY_FORMAT, totalPrice)
        };
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateTransactionHistoryEvent historyEvent) {
            System.out.println("TransactionHistoryPanel received UpdateTransactionHistoryEvent");
            SwingUtilities.invokeLater(() -> updateTransactionTable(historyEvent.getTransactionHistory()));
        }
    }
}
