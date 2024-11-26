package view.panels;

import entity.Transaction;
import entity.TransactionHistory;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import view.FontManager;
import view.IComponent;
import view.ViewManager;
import view.components.ButtonComponent;
import view.components.TableComponent;
import view.view_events.SwitchPanelEvent;
import view.view_events.UpdateTransactionHistoryEvent;
import view.view_events.ViewEvent;

public class TransactionHistoryPanel extends JPanel implements IComponent {

  private static final int PADDING = 20;
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
  private static final String[] COLUMN_NAMES = {
      "Date", "Ticker", "Action", "Price", "Quantity", "Total Price"
  };
  private static final double[] COLUMN_PROPORTIONS = {
      0.25, // Date
      0.15, // Ticker
      0.10, // Action
      0.15, // Price
      0.15, // Quantity
      0.20 // Total Price
  };

  private final TableComponent historyTable;
  private final DefaultTableModel tableModel;

  public TransactionHistoryPanel() {
    ViewManager.Instance().registerComponent(this);

    // Set up main panel
    setLayout(new BorderLayout(0, PADDING));
    setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

    // Create table model and table
    tableModel = createTableModel();
    historyTable = new TableComponent(tableModel, COLUMN_PROPORTIONS);
    FontManager.Instance().useRegular(historyTable, 14f);

    // Create and add header
    add(createHeaderPanel(), BorderLayout.NORTH);

    // Create and add table panel
    JScrollPane scrollPane = new JScrollPane(historyTable);
    add(scrollPane, BorderLayout.CENTER);

    // Add resize listener
    addComponentListener(
        new java.awt.event.ComponentAdapter() {
          @Override
          public void componentResized(java.awt.event.ComponentEvent evt) {
            historyTable.adjustColumnWidths();
          }
        });
  }

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel(new BorderLayout());

    // Title on the left
    JLabel titleLabel = new JLabel("Transaction History");
    FontManager.Instance().useBold(titleLabel, 24f);
    headerPanel.add(titleLabel, BorderLayout.WEST);

    // Back button on the right
    ButtonComponent backButton = new ButtonComponent("Back to Home");
    FontManager.Instance().useRegular(backButton, 14f);
    backButton.addActionListener(
        e -> ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel")));
    headerPanel.add(backButton, BorderLayout.EAST);

    return headerPanel;
  }

  private DefaultTableModel createTableModel() {
    return new DefaultTableModel(COLUMN_NAMES, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
  }

  private void updateTableData(TransactionHistory history) {
    tableModel.setRowCount(0);
    if (history != null) {
      for (Transaction transaction : history.getTransactions()) {
        double totalPrice = transaction.executionPrice() * transaction.quantity();
        tableModel.addRow(new Object[]{
            DATE_FORMAT.format(transaction.timestamp()),
            transaction.ticker(),
            transaction.type(),
            String.format("$%.2f", transaction.executionPrice()),
            transaction.quantity(),
            String.format("$%.2f", totalPrice)
        });
      }
    }
  }

  @Override
  public void receiveViewEvent(ViewEvent event) {
    if (event instanceof UpdateTransactionHistoryEvent historyEvent) {
      updateTableData(historyEvent.getTransactionHistory());
    }
  }
}
