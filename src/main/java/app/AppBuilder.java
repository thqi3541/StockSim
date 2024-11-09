package app;

import javax.swing.*;
import java.awt.*;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * out CA architecture; piece by piece.
 * <p/>
 * This is done by adding each View and then adding related Use Cases.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();

    public AppBuilder() {
        CardLayout cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the Buy View to the application.
     *
     * @return this builder
     */
    public AppBuilder addBuyView() {
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Buy Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}
