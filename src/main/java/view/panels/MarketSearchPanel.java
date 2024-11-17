package view.panels;

import entity.Stock;
import utility.ViewManager;
import view.IComponent;
import view.view_events.EventType;
import view.view_events.UpdateStockEvent;
import view.view_events.UpdateUsernameEvent;
import view.view_events.ViewEvent;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.EnumSet;
import java.util.Objects;
import java.util.List;

public class MarketSearchPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 10;
    private static final int PANEL_HEIGHT = 400;
    private static final int ROW_HEIGHT = 30;
    private static final int HEADER_HEIGHT = 30;
    private static final int SEARCH_FIELD_COLUMNS = 20;

    // Column Width Constants
    private static final double[] COLUMN_PROPORTIONS = {0.20, 0.40, 0.30, 0.10}; // Proportions for each column

    // Font Constants
    private static final Font TITLE_FONT = new Font("Lucida Sans", Font.BOLD, 24);
    private static final Font SEARCH_FONT = new Font("Lucida Sans", Font.PLAIN, 14);

    // Text Constants
    private static final String TITLE_TEXT = "Market Overview";
    private static final String SEARCH_BUTTON_TEXT = "Search";
    private static final String SEARCH_PLACEHOLDER = "Ticker, company, or industry";
    private static final String[] COLUMN_NAMES = {"Ticker", "Company Name", "Industry", "Price"};

    // Mock Data
    private static final Object[][] INITIAL_DATA = {
            {"AAPL", "Apple Inc.", "Technology", "150.00"},
            {"GOOG", "Alphabet Inc.", "Technology", "2800.00"},
            {"TSLA", "Tesla Inc.", "Automotive", "900.00"},
            {"MSFT", "Microsoft Corporation", "Technology", "320.50"},
            {"AMZN", "Amazon.com Inc.", "E-Commerce", "3200.00"},
            {"META", "Meta Platforms Inc.", "Technology", "330.45"},
            {"NFLX", "Netflix Inc.", "Entertainment", "550.20"}
    };

    private final JTextField searchField;
    private final JButton searchButton;
    private final JLabel titleLabel;
    private final JTable stockTable;
    private final TableRowSorter<TableModel> rowSorter;

    public MarketSearchPanel() {
        ViewManager.Instance().registerComponent(this);
        setupPanel();

        // Initialize components
        titleLabel = createTitleLabel();
        searchField = createSearchField();
        searchButton = createSearchButton();
        stockTable = createStockTable();
        rowSorter = new TableRowSorter<>(stockTable.getModel());
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

    private void setupPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, PANEL_HEIGHT)); // Only fix the height
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
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
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // Set horizontal gap to 0
        searchPanel.add(searchField);

        // Add a small gap to the right of the search field explicitly
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
        String[] columnNames = {"Ticker", "Company Name", "Price", "Change"};
        Object[][] data = {
                {"AAPL", "Apple Inc.", "150.00", "+1.23"},
                {"GOOG", "Alphabet Inc.", "2800.00", "-15.23"},
                {"TSLA", "Tesla Inc.", "900.00", "+12.34"}
        };
        return new DefaultTableModel(data, columnNames) {
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

        // Set auto resize mode
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
            // Filter that checks ticker, company name, and industry
            rowSorter.setRowFilter(RowFilter.orFilter(List.of(
                    RowFilter.regexFilter("(?i)" + searchText, 0), // Ticker column
                    RowFilter.regexFilter("(?i)" + searchText, 1), // Company name column
                    RowFilter.regexFilter("(?i)" + searchText, 2)  // Industry column
            )));
        }
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
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.noneOf(EventType.class);
    }
}
