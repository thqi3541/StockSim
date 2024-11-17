package view.panels;

import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.components.InputComponent;
import view.components.PasswordInputComponent;
import view.view_events.EventType;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class LogInPanel extends JPanel implements IComponent {
    // Components
    private final InputComponent usernameInput;
    private final PasswordInputComponent passwordInput;
    private final ButtonComponent logInButton;
    private final ButtonComponent signUpButton;

    public LogInPanel() {
        // Initialize components
        usernameInput = new InputComponent("Username", 20);
        passwordInput = new PasswordInputComponent("Password", 20);
        logInButton = new ButtonComponent("Log In");
        signUpButton = new ButtonComponent("Go to Sign Up");

        // Register this panel as a component in ViewManager
        ViewManager.Instance().registerComponent(this);

        // Set up the panel layout
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components
        add(createHeader(), BorderLayout.NORTH);
        add(createCenteredFormPanel(), BorderLayout.CENTER);

        // Configure button actions
        configureButtonActions();
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // 20px bottom spacing

        // Add "Go to Sign Up" button aligned to the right
        headerPanel.add(signUpButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCenteredFormPanel() {
        // Create a wrapper panel to center the form panel
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE; // Prevent filling the parent
        gbc.anchor = GridBagConstraints.CENTER; // Center the content

        centeringPanel.add(createOuterFormPanel(), gbc);
        return centeringPanel;
    }

    private JPanel createOuterFormPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setPreferredSize(new Dimension(300, 300)); // Fixed dimensions for the outer panel
        outerPanel.setMaximumSize(new Dimension(300, 300));

        // Add title, input fields, and button as separate sections
        outerPanel.add(createTitleSection());
        outerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing between sections
        outerPanel.add(createInputSection());
        outerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing between sections
        outerPanel.add(createButtonSection());

        return outerPanel;
    }

    private JPanel createTitleSection() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("StockSim");
        titleLabel.setFont(new Font("Lucida Sans", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        return titlePanel;
    }

    private JPanel createInputSection() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add username input
        inputPanel.add(usernameInput);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between inputs

        // Add password input
        inputPanel.add(passwordInput);

        // Ensure natural height by not explicitly setting preferred size
        usernameInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameInput.getPreferredSize().height));
        passwordInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordInput.getPreferredSize().height));

        return inputPanel;
    }

    private JPanel createButtonSection() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logInButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(logInButton);

        return buttonPanel;
    }

    private void configureButtonActions() {
        logInButton.addActionListener(e -> {
            // TODO: Add logic for logging in
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel"));
        });

        signUpButton.addActionListener(e -> {
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("SignUpPanel"));
        });
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent) {
            System.out.println("LogInPanel received a SwitchPanelEvent to switch panels.");
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        return EnumSet.of(EventType.SWITCH_PANEL);
    }
}