package view.components;

import view.FontManager;

import javax.swing.*;
import java.awt.*;

public class ButtonComponent extends JButton {

    public ButtonComponent(String text) {
        super(text);

        // Set font and colors
        FontManager.Instance().useRegular(this, 14f);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);

        // Set size and padding
        Dimension size = new Dimension(getPreferredSize().width, 40);
        setPreferredSize(size);
        setMargin(new Insets(5, 5, 5, 5));
    }
}
