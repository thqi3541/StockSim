package view.panels;

import interface_adapter.execute_buy.ExecuteBuyController;
import utility.ServiceManager;
import view.FontManager;
import view.IComponent;
import view.ViewManager;
import view.components.ButtonComponent;
import view.components.InputComponent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;

public class OrderEntryPanel extends JPanel implements IComponent {
    private static final int HEADER_HEIGHT = 40;
    private static final int PADDING = 20;
    private static final int INNER_GAP = 10;

    private final InputComponent tickerInput;
    private final InputComponent quantityInput;
    private final JButton buyButton;
    private final JButton sellButton;

    public OrderEntryPanel() {
        ViewManager.Instance().registerComponent(this);

        // Initialize components
        tickerInput = new InputComponent("Ticker", 15);
        quantityInput = new InputComponent("Quantity", 15);
        buyButton = new ButtonComponent("Buy");
        sellButton = new ButtonComponent("Sell");

        // Set up panel layout
        setLayout(new BorderLayout(0, PADDING));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, PADDING));
        contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add input section at the top
        JPanel inputPanel = createInputPanel();
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Add gap
        contentPanel.add(Box.createRigidArea(new Dimension(0, Integer.MAX_VALUE)));

        // Add button section at the bottom
        JPanel buttonPanel = createButtonPanel();
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        // Create main header panel with fixed height
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));

        // Title with vertical centering
        JLabel titleLabel = new JLabel("Order Entry");
        FontManager.Instance().useBold(titleLabel, 18f);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Center the title vertically
        headerPanel.add(Box.createVerticalGlue());
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalGlue());

        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // Configure quantity input
        FontManager.Instance().useRegular(tickerInput, 14f);
        tickerInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, tickerInput.getPreferredSize().height));
        tickerInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.add(tickerInput);

        // Add gap between inputs
        inputPanel.add(Box.createRigidArea(new Dimension(0, INNER_GAP)));

        // Configure executionPrice input
        FontManager.Instance().useRegular(quantityInput, 14f);
        quantityInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, quantityInput.getPreferredSize().height));
        quantityInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputPanel.add(quantityInput);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, PADDING, 0));

        FontManager.Instance().useRegular(buyButton, 14f);
        buttonPanel.add(buyButton);
        buyButton.addActionListener(e -> handleBuyAction());

        // TODO: add listener to call sell controller
        FontManager.Instance().useRegular(sellButton, 14f);
        buttonPanel.add(sellButton);

        return buttonPanel;
    }

    private void handleBuyAction() {
        String ticker = tickerInput.getText();
        String quantity = quantityInput.getText();

        // Execute buy action
        ExecuteBuyController controller = ServiceManager.Instance()
                .getService(ExecuteBuyController.class);
        controller.execute(ticker, quantity);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
    }
}
