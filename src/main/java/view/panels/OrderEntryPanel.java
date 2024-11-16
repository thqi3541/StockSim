package view.panels;

import entity.Stock;
import entity.User;
import entity.UserStock;
import interface_adapter.execute_buy.ExecuteBuyController;
import utility.ServiceManager;
import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.components.InputComponent;
import view.view_events.EventType;
import view.view_events.UpdateAssetEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class OrderEntryPanel extends JPanel implements IComponent {
    // Layout Constants
    private static final int BORDER_PADDING = 10;
    private static final int MIN_WIDTH = 150;
    private static final int MIN_HEIGHT = 300;
    private static final int PREF_WIDTH = 150;
    private static final int PREF_HEIGHT = 400;
    private static final Insets FIELD_INSETS = new Insets(10, 0, 10, 0);
    private static final int BUTTON_GAP = 10;

    // Font Constants
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);

    // Text Constants
    private static final String TITLE_TEXT = "Order Entry";
    private static final String TICKER_LABEL = "Ticker";
    private static final String QUANTITY_LABEL = "Quantity";
    private static final String BUY_BUTTON_TEXT = "Buy";
    private static final String SELL_BUTTON_TEXT = "Sell";

    // Component Size Constants
    private static final int INPUT_COLUMNS = 10;
    private static final double GRID_WEIGHT = 1.0;

    // Components
    private final JLabel titleLabel;
    private final InputComponent tickerField;
    private final InputComponent quantityField;
    private final JButton buyButton;
    private final JButton sellButton;

    public OrderEntryPanel() {
        ViewManager.Instance().registerComponent(this);
        setupPanel();

        // Initialize components
        titleLabel = createTitleLabel();
        tickerField = new InputComponent(TICKER_LABEL, INPUT_COLUMNS);
        quantityField = new InputComponent(QUANTITY_LABEL, INPUT_COLUMNS);
        buyButton = createBuyButton();
        sellButton = createSellButton();

        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    // Test main method
    public static void main(String[] args) {
        JFrame frame = new JFrame("Order Entry Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(new OrderEntryPanel());
        frame.setVisible(true);
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
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = FIELD_INSETS;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = GRID_WEIGHT;

        gbc.gridy = 0;
        formPanel.add(tickerField, gbc);
        gbc.gridy = 1;
        formPanel.add(quantityField, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, BUTTON_GAP, 0));
        buttonPanel.add(buyButton);
        buttonPanel.add(sellButton);
        return buttonPanel;
    }

    private JButton createBuyButton() {
        ButtonComponent button = new ButtonComponent(BUY_BUTTON_TEXT);
        button.addActionListener(e -> handleBuyAction());
        return button;
    }

    private JButton createSellButton() {
        return new ButtonComponent(SELL_BUTTON_TEXT);
    }

    private void handleBuyAction() {
        String ticker = tickerField.getText();
        String quantity = quantityField.getText();

        // Execute buy action
        ExecuteBuyController controller = ServiceManager.Instance()
                .getService(ExecuteBuyController.class);
        controller.execute(ticker, quantity);

        // Test update asset (mock data)
        User user = createTestUser(ticker, quantity);
        ViewManager.Instance().broadcastEvent(
                new UpdateAssetEvent(user.getPortfolio(), user.getBalance()));
    }

    private User createTestUser(String ticker, String quantity) {
        User user = new User("user1", "password");
        user.addBalance(10000.00);

        Stock stock1 = new Stock(ticker, "XXX", "XXX", 100.00);
        Stock stock2 = new Stock("AAPL", "YYY", "YYY", 200.00);
        user.getPortfolio().addStock(new UserStock(stock1, 100.00, Integer.parseInt(quantity)));
        user.getPortfolio().addStock(new UserStock(stock2, 200.00, 10));

        return user;
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        // Placeholder for future event handling
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.noneOf(EventType.class);
    }
}
