package view.panels;

import view.FontManager;
import view.IComponent;
import view.ViewManager;
import view.components.ButtonComponent;
import view.components.InputComponent;
import view.components.PasswordInputComponent;
import view.view_events.DialogEvent;
import view.view_events.SwitchPanelEvent;
import view.view_events.ViewEvent;

import javax.swing.*;
import java.awt.*;

public class SignUpPanel extends JPanel implements IComponent {
    private final InputComponent usernameField;
    private final PasswordInputComponent passwordField;
    private final PasswordInputComponent repeatPasswordField;
    private final ButtonComponent signUpButton;
    private final ButtonComponent logInButton;

    public SignUpPanel() {
        usernameField = new InputComponent("Username", 20);
        passwordField = new PasswordInputComponent("Password", 20);
        repeatPasswordField = new PasswordInputComponent("Repeat password", 20);
        signUpButton = new ButtonComponent("Sign Up");
        logInButton = new ButtonComponent("Go to Log In");

        ViewManager.Instance().registerComponent(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createCenteredFormPanel(), BorderLayout.CENTER);

        configureButtonActions();
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        headerPanel.add(logInButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createCenteredFormPanel() {
        JPanel centeringPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centeringPanel.add(createOuterFormPanel(), gbc);
        return centeringPanel;
    }

    private JPanel createOuterFormPanel() {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

        outerPanel.add(createTitleSection());
        outerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        outerPanel.add(createInputSection());
        outerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        outerPanel.add(createButtonSection());

        return outerPanel;
    }

    private JPanel createTitleSection() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Registration");
        FontManager.Instance().useBold(titleLabel, 24f);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        return titlePanel;
    }

    private JPanel createInputSection() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel.add(usernameField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(passwordField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(repeatPasswordField);

        usernameField.setMaximumSize(new Dimension(300, usernameField.getPreferredSize().height));
        passwordField.setMaximumSize(new Dimension(300, passwordField.getPreferredSize().height));
        repeatPasswordField.setMaximumSize(new Dimension(300, repeatPasswordField.getPreferredSize().height));

        return inputPanel;
    }

    private JPanel createButtonSection() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(signUpButton);
        return buttonPanel;
    }

    private void configureButtonActions() {
        signUpButton.addActionListener(e -> {
            ViewManager.Instance().broadcastEvent(new DialogEvent("Sign Up", "WIP"));
        });

        logInButton.addActionListener(e -> {
            ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel"));
        });
    }

    @Override
    public void receiveViewEvent(ViewEvent event) {
    }
}
