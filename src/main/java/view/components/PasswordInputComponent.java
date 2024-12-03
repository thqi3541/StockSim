package view.components;

import java.awt.*;
import javax.swing.*;
import view.FontManager;

/** A component that wraps a JPasswordField with optional label */
public class PasswordInputComponent extends JPanel {
    private final JPasswordField passwordField;

    /**
     * Constructor with label on top
     *
     * @param labelText text for the label
     * @param columns number of columns for the password field
     */
    public PasswordInputComponent(String labelText, int columns) {
        setLayout(new BorderLayout(0, 5)); // Add a vertical gap between label and field
        JLabel label = new JLabel(labelText);
        passwordField = new JPasswordField(columns);

        // Set font for both label and password field
        FontManager fontManager = FontManager.Instance();
        fontManager.useRegular(label, 14f);
        fontManager.useRegular(passwordField, 14f);

        label.setHorizontalAlignment(SwingConstants.LEFT); // Align label to the left
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 40));

        add(label, BorderLayout.NORTH);
        add(passwordField, BorderLayout.CENTER);
    }

    /**
     * Constructor for field only (no label)
     *
     * @param columns number of columns for the password field
     */
    public PasswordInputComponent(int columns) {
        setLayout(new BorderLayout());
        passwordField = new JPasswordField(columns);
        FontManager fontManager = FontManager.Instance();
        fontManager.useRegular(passwordField, 14f);
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 40));
        add(passwordField, BorderLayout.CENTER);
    }

    /**
     * Constructor with placeholder text, no label
     *
     * @param placeholderText placeholder text to display
     */
    public PasswordInputComponent(String placeholderText) {
        setLayout(new BorderLayout());
        passwordField = new JPasswordField();
        FontManager fontManager = FontManager.Instance();
        fontManager.useRegular(passwordField, 14f);
        passwordField.setEchoChar((char) 0); // Temporarily show the placeholder text
        passwordField.setText(placeholderText);
        passwordField.setPreferredSize(new Dimension(passwordField.getPreferredSize().width, 40));

        // Add focus listener to handle placeholder behavior
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholderText)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('●'); // Set back to password dots
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholderText);
                    passwordField.setEchoChar((char) 0); // Show placeholder text
                }
            }
        });

        add(passwordField, BorderLayout.CENTER);
    }

    /**
     * Gets the password as a char array
     *
     * @return the password as a char array
     */
    public char[] getPassword() {
        return passwordField.getPassword();
    }

    /**
     * Sets the password field text
     *
     * @param text the text to set
     */
    public void setText(String text) {
        passwordField.setText(text);
    }

    /** Clears the password field */
    public void clear() {
        passwordField.setText("");
    }
}
