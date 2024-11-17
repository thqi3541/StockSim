package view.panels;

import entity.Transaction;
import entity.TransactionHistory;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.view_events.EventType;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;
import view.view_events.ViewHistoryEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.EnumSet;

/**
 * A panel containing the ViewHistory use case page.
 */
public class TransactionHistoryPanel extends JPanel implements IComponent {
    private DefaultTableModel tableModel;

    public TransactionHistoryPanel() {
        ViewManager.Instance().registerComponent(this);

        // Page layout
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(600, 400));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel with Title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Back to Home button
        ButtonComponent backButton = new ButtonComponent("Back to Home");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e ->
                ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel"))
        );

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.add(backButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Creates base Transaction History Table
        tableModel = new DefaultTableModel(new Object[][]{}, getColumnNames()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Transaction History Table setup
        JTable historyTable = new JTable(tableModel);
        historyTable.setFillsViewportHeight(true);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 14));
        historyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        historyTable.getTableHeader().setForeground(Color.GRAY);
        historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add scroll
        JScrollPane tableScrollPane = new JScrollPane(historyTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 200));
        add(tableScrollPane, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Transaction History");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        TransactionHistoryPanel historyPanel = new TransactionHistoryPanel();
        frame.add(historyPanel);
        frame.setVisible(true);
    }

    /**
     * Displays default data to test correct display format
     */
    private DefaultTableModel getDefaultTableModel() {
        String[] columnNames = getColumnNames();

        Object[][] data = {
                {"Tue Nov 01 02:37:18", "NVDA", "NVIDIA Corporation", "Technology", "Bought", "138.27", "2", "276.54"},
                {"XXX XXX XX XX:XX:XX", "XXXX", "XXXXXXXXXXXXXXXXXX", "Health", "XXXX", "XXX.XX", "XX", "XXX.XX"},
                {"XXX XXX XX XX:XX:XX", "XXXX", "XXXXXXXXXXXXXXXXXX", "Financial", "XXXX", "XXX.XX", "XX", "XXX.XX"},
        };

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Gets the set table column names
     *
     * @return column names
     */
    private String[] getColumnNames() {
        return new String[]{"Date", "Ticker", "Company Name", "Industry", "Action", "Price", "Quantity", "Total Price"};
    }

    /**
     * Creates new table entry based on transaction data
     *
     * @param transaction the transaction to be displayed
     * @return new table row data
     */
    private Object[] createRowData(Transaction transaction) {
        // Retrieves data from transaction
        String ticker = transaction.getTicker();
        Date date = transaction.getTimestamp();
        String company = "Unknown Company";
        String industry = "Unknown Industry";
        String action = transaction.getType();
        double price = transaction.getPrice();
        int quantity = transaction.getQuantity();
        double totalPrice = price * quantity;

        // Creates new formatted row data
        return new Object[]{
                date.toString(),
                ticker,
                company,
                industry,
                action,
                String.format("%.2f", price),
                quantity,
                String.format("%.2f", totalPrice),
        };
    }

    /**
     * Handles a received event.
     *
     * @param event The event to handle.
     */
    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof ViewHistoryEvent updateEvent) {
            // Retrieves transaction history
            TransactionHistory transactionHistory = updateEvent.getTransactionHistory();

            // Resets the table
            tableModel.setRowCount(0);

            // Stores all current transaction history data in table
            transactionHistory.getAllTransactions().forEach(transaction -> {
                Object[] rowData = createRowData(transaction);
                tableModel.addRow(rowData);
            });
        }
    }
}
