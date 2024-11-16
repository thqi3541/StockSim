package view.panels;

import utility.ViewManager;
import view.IComponent;
import view.view_events.EventType;
import view.view_events.ViewEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.EnumSet;
import java.util.List;

public class MarketSearchPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 10;
    private static final int MIN_WIDTH = 400;
    private static final int MIN_HEIGHT = 300;
    private static final int PREF_WIDTH = 600;
    private static final int PREF_HEIGHT = 400;
    private static final int SEARCH_FIELD_COLUMNS = 20;

    // Font Constants
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font SEARCH_FONT = new Font("Arial", Font.PLAIN, 14);

    // Text Constants
    private static final String TITLE_TEXT = "Market Search";
    private static final String SEARCH_BUTTON_TEXT = "Search";
    private static final String SEARCH_PLACEHOLDER = "Ticker, company name, or industry";
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
    }

    // Test main method
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Market Search Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new MarketSearchPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));
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
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.add(searchField);
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
        DefaultTableModel model = new DefaultTableModel(INITIAL_DATA, COLUMN_NAMES) {
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
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(SEARCH_FONT);
        table.getTableHeader().setFont(SEARCH_FONT);

        return table;
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

    @Override
    public void receiveViewEvent(ViewEvent event) {
        // Set up for future event handling
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.noneOf(EventType.class);
    }
}