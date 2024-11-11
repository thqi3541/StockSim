package view.main_panels.authentication;

import view.IComponent;
import view.ViewManager;
import view.view_event.EventType;
import view.view_event.SwitchPanelEvent;
import view.view_event.ViewEvent;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

public class LogInPanel extends JPanel implements IComponent {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton logInButton;

    public LogInPanel() {
        setLayout(new BorderLayout());

        setMinimumSize(new Dimension(150, 300));
        setPreferredSize(new Dimension(250, 400));

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Log In");
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

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        gbc.gridy = 0;
        formPanel.add(new JLabel("Username"), gbc);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password"), gbc);
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        logInButton = new JButton("Log In");
        logInButton.setBackground(Color.LIGHT_GRAY);
        logInButton.setForeground(Color.BLACK);

        // Action listener to switch to DashboardPanel on successful login
        logInButton.addActionListener(e -> {
            // Broadcast a SwitchPanelEvent targeting "DashboardPanel"
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("DashboardPanel"));
        });

        gbc.gridy = 4;
        formPanel.add(logInButton, gbc);

        // "Go to Sign Up" button
        JButton signUpButton = new JButton("Go to Sign Up");
        logInButton.setBackground(Color.LIGHT_GRAY);
        logInButton.setForeground(Color.BLACK);

        // Action listener for switching to SignUpPanel
        signUpButton.addActionListener(e -> {
            // Create and broadcast SwitchPanelEvent targeting "SignUpPanel"
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("SignUpPanel"));
        });

        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(signUpButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
        if (event instanceof SwitchPanelEvent) {
            // Optionally, log receipt of a SwitchPanelEvent or update UI as needed
            System.out.println("LogInPanel received a SwitchPanelEvent to switch panels.");
        }
    }

    @Override
    public EnumSet<EventType> getSupportedEventTypes() {
        // LogInPanel only supports SWITCH_PANEL events
        return EnumSet.of(EventType.SWITCH_PANEL);
    }
}