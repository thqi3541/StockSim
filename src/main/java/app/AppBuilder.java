package app;

import interface_adapter.execute_buy.ExecuteBuyViewModel;
import use_case.execute_buy.ExecuteBuyInteractor;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * out CA architecture; piece by piece.
 * <p/>
 * This is done by adding each View and then adding related Use Cases.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the Buy View to the application.
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
