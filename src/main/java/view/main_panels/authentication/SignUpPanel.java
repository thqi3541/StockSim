package view.main_panels.authentication;

import view.IComponent;
import view.ViewManager;
import view.view_event.EventType;
import view.view_event.SwitchPanelEvent;
import view.view_event.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class SignUpPanel extends JPanel implements IComponent {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField repeatPasswordField;
    private final JButton signUpButton;

    public SignUpPanel() {
        setLayout(new BorderLayout());

        setMinimumSize(new Dimension(150, 300));
        setPreferredSize(new Dimension(250, 400));

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Create a new account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Username, Password, Repeat Password fields
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        repeatPasswordField = new JPasswordField();

        gbc.gridy = 0;
        formPanel.add(new JLabel("Username"), gbc);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);
        gbc.gridy = 4;
        formPanel.add(new JLabel("Repeat password"), gbc);
        gbc.gridy = 5;
        formPanel.add(repeatPasswordField, gbc);

        // Sign Up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(Color.LIGHT_GRAY);
        signUpButton.setForeground(Color.BLACK);

        gbc.gridy = 6;
        formPanel.add(signUpButton, gbc);

        // "Go to Log In" button
        JButton logInButton = new JButton("Go to Log In");
        logInButton.setBackground(Color.LIGHT_GRAY);
        logInButton.setForeground(Color.BLACK);

        // Action listener for switching to LogInPanel
        logInButton.addActionListener(e -> {
            // Broadcast a SwitchPanelEvent to switch to "LogInPanel"
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel"));
        });

        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(logInButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent) {
            System.out.println("SignUpPanel received a SwitchPanelEvent.");
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        // SignUpPanel only supports SWITCH_PANEL events
        return EnumSet.of(EventType.SWITCH_PANEL);
    }
}