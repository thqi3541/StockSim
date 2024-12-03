package view.components;

import java.awt.*;
import javax.swing.*;
import view.FontManager;

public class InputComponent extends JPanel {
    private final JTextField textField;

    // Constructor with label on top
    public InputComponent(String labelText, int columns) {
        setLayout(new BorderLayout(0, 5)); // Add a vertical gap between label and field
        JLabel label = new JLabel(labelText);
        textField = new JTextField(columns);

        // Set font for both label and text field
        FontManager fontManager = FontManager.Instance();
        fontManager.useRegular(label, 14f);
        fontManager.useRegular(textField, 14f);

        label.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 40));

        add(label, BorderLayout.NORTH);
        add(textField, BorderLayout.CENTER);
    }

    // Constructor for field only (no label)
    public InputComponent(int columns) {
        setLayout(new BorderLayout());
        textField = new JTextField(columns);
        FontManager.Instance().useRegular(textField, 14f);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 40));
        add(textField, BorderLayout.CENTER);
    }

    // Constructor with placeholder text, no label
    public InputComponent(String placeholderText) {
        setLayout(new BorderLayout());
        textField = new JTextField();
        FontManager.Instance().useRegular(textField, 14f);
        textField.setText(placeholderText);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 40));
        add(textField, BorderLayout.CENTER);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void clear() {
        textField.setText("");
    }
}
