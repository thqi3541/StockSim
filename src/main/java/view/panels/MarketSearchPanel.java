package view.panels;

import entity.Stock;
import org.jetbrains.annotations.NotNull;
import utility.ViewManager;
import view.IComponent;
import view.view_events.UpdateStockEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class MarketSearchPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 10;
    private static final int PANEL_HEIGHT = 400;
    private static final int ROW_HEIGHT = 30;
    private static final int HEADER_HEIGHT = 30;
    private static final int SEARCH_FIELD_COLUMNS = 20;

    // Column Width Constants
    private static final double[] COLUMN_PROPORTIONS = {0.20, 0.40, 0.30, 0.10};

    // Font Constants
    private static final Font TITLE_FONT = new Font("Lucida Sans", Font.BOLD, 24);
    private static final Font SEARCH_FONT = new Font("Lucida Sans", Font.PLAIN, 14);

    // Text Constants
    private static final String TITLE_TEXT = "Market Overview";
    private static final String SEARCH_BUTTON_TEXT = "Search";
    private static final String SEARCH_PLACEHOLDER = "Ticker, company, or industry";
    private static final String[] COLUMN_NAMES = {"Ticker", "Company Name", "Industry", "Price"};

    private final JTextField searchField;
    private final JButton searchButton;
    private final JLabel titleLabel;
    private final JTable stockTable;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public MarketSearchPanel() {
        ViewManager.Instance().registerComponent(this);
        setupPanel();

        // Initialize components
        titleLabel = createTitleLabel();
        searchField = createSearchField();
        searchButton = createSearchButton();
        stockTable = createStockTable();
        rowSorter = new TableRowSorter<>((DefaultTableModel) stockTable.getModel());
        stockTable.setRowSorter(rowSorter);

        // Add components to panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);

        // Add resize listener
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustColumnWidths();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Market Search Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        MarketSearchPanel marketSearchPanel = new MarketSearchPanel();
        frame.add(marketSearchPanel);
        frame.setVisible(true);
    }

    @NotNull
    private static DefaultTableModel getTableModel(List<Stock> stocks) {
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

        // 2. Add data to model
        for (Stock stock : stocks) {
            Object[] row = new Object[]{
                    stock.getTicker(),
                    stock.getCompany(),
                    stock.getIndustry(),
                    String.format("%.2f", stock.getPrice())
            };
            newModel.addRow(row);
        }
        return newModel;
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setBorder(BorderFactory.createEmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel(TITLE_TEXT);
        label.setFont(TITLE_FONT);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JTextField createSearchField() {
        JTextField field = new JTextField(SEARCH_FIELD_COLUMNS);
        field.setFont(SEARCH_FONT);
        field.setText(SEARCH_PLACEHOLDER);
        field.setForeground(Color.GRAY);

        // Add placeholder text behavior
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(SEARCH_PLACEHOLDER)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(SEARCH_PLACEHOLDER);
                    field.setForeground(Color.GRAY);
                }
            }
        });

        // Add search on Enter key
        field.addActionListener(e -> performSearch());

        return field;
    }

    private JButton createSearchButton() {
        JButton button = new JButton(SEARCH_BUTTON_TEXT);
        button.setFont(SEARCH_FONT);
        button.addActionListener(e -> performSearch());
        return button;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, BORDER_PADDING, 0));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(createSearchPanel(), BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel(new BorderLayout());
        JScrollPane tableScrollPane = new JScrollPane(stockTable);
        bodyPanel.add(tableScrollPane, BorderLayout.CENTER);
        return bodyPanel;
    }

    private JTable createStockTable() {
        DefaultTableModel model = new DefaultTableModel(new Object[0][COLUMN_NAMES.length], COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(ROW_HEIGHT);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(SEARCH_FONT);
        table.getTableHeader().setFont(SEARCH_FONT);
        table.getTableHeader().setPreferredSize(
                new Dimension(table.getTableHeader().getPreferredSize().width, HEADER_HEIGHT));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        return table;
    }

    private void adjustColumnWidths() {
        int availableWidth = getWidth() - (2 * BORDER_PADDING);
        if (availableWidth <= 0) return;

        for (int col = 0; col < stockTable.getColumnCount(); col++) {
            TableColumn column = stockTable.getColumnModel().getColumn(col);
            int columnWidth = (int) (COLUMN_PROPORTIONS[col] * availableWidth);
            column.setPreferredWidth(columnWidth);
        }
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.equals(SEARCH_PLACEHOLDER) || searchText.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }

    private void updateStockTable(List<Stock> stocks) {
        SwingUtilities.invokeLater(() -> {
            if (stocks == null || stocks.isEmpty()) {
                System.err.println("No stocks available to update.");
                DefaultTableModel emptyModel = new DefaultTableModel(new Object[][]{
                        {"Loading...", "Please wait while stock data is being fetched...", "", ""}
                }, COLUMN_NAMES) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                stockTable.setModel(emptyModel);
                stockTable.setEnabled(false);
                return;
            }

            // Enable table and update with real data
            stockTable.setEnabled(true);

            // 1. Create new table model with data
            DefaultTableModel newModel = getTableModel(stocks);

            // 3. Update table model
            stockTable.setModel(newModel);

            // 4. Create and set new row sorter
            rowSorter = new TableRowSorter<>(newModel);
            stockTable.setRowSorter(rowSorter);

            // 5. Reapply current search filter if exists
            String searchText = searchField.getText().trim();
            if (!searchText.equals(SEARCH_PLACEHOLDER) && !searchText.isEmpty()) {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
            }

            // 6. Reapply column widths
            adjustColumnWidths();

            // 7. Repaint the table to ensure visual update
            stockTable.revalidate();
            stockTable.repaint();
        });
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof UpdateStockEvent stockEvent) {
            System.out.println("MarketSearchPanel received UpdateStockEvent with stocks: " +
                    (stockEvent.getStocks() != null ? stockEvent.getStocks().size() : "null"));
            updateStockTable(stockEvent.getStocks());
        }
    }
}