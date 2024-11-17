package view.components;

import javax.swing.*;
import java.awt.*;

public class ButtonComponent extends JButton {

    public ButtonComponent(String text) {
        super(text);

        // Set font and colors
        setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        // Set size and padding
        Dimension size = new Dimension(getPreferredSize().width, 40);
        setPreferredSize(size);
        setMargin(new Insets(5, 5, 5, 5));
    }
}