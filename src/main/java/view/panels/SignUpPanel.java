package view.panels;

import utility.ViewManager;
import view.IComponent;
import view.components.ButtonComponent;
import view.components.InputComponent;
import view.components.PasswordInputComponent;
import view.view_events.DialogEvent;
import view.view_events.EventType;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class SignUpPanel extends JPanel implements IComponent {
    // Components
    private final InputComponent usernameField;
    private final PasswordInputComponent passwordField;
    private final PasswordInputComponent repeatPasswordField;
    private final ButtonComponent signUpButton;
    private final ButtonComponent logInButton;

    public SignUpPanel() {
        // Initialize components
        usernameField = new InputComponent("Username", 20);
        passwordField = new PasswordInputComponent("Password", 20);
        repeatPasswordField = new PasswordInputComponent("Repeat password", 20);
        signUpButton = new ButtonComponent("Sign Up");
        logInButton = new ButtonComponent("Go to Log In");

        // Register this panel as a component in ViewManager
        ViewManager.Instance().registerComponent(this);

        // Set up the panel layout and border
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

        // Add "Go to Log In" button aligned to the right
        headerPanel.add(logInButton, BorderLayout.EAST);

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
        outerPanel.setPreferredSize(new Dimension(300, 350)); // Fixed dimensions for the outer panel
        outerPanel.setMaximumSize(new Dimension(300, 350));

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

        JLabel titleLabel = new JLabel("Create a new account");
        titleLabel.setFont(new Font("Lucida Sans", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        return titlePanel;
    }

    private JPanel createInputSection() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add username field
        inputPanel.add(usernameField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between inputs

        // Add password fields
        inputPanel.add(passwordField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between inputs
        inputPanel.add(repeatPasswordField);

        // Ensure natural height by not explicitly setting preferred size
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        repeatPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, repeatPasswordField.getPreferredSize().height));

        return inputPanel;
    }

    private JPanel createButtonSection() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        signUpButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(signUpButton);

        return buttonPanel;
    }

    private void configureButtonActions() {
        signUpButton.addActionListener(e -> {
            // Simulate a dialog event for successful sign-up
            ViewManager.Instance().broadcastEvent(new DialogEvent("Sign Up", "Sign Up button clicked."));
        });

        logInButton.addActionListener(e -> {
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel"));
        });
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent || event instanceof DialogEvent) {
            System.out.println("SignUpPanel received an event: " + event.getClass().getSimpleName());
        }
    }
}
