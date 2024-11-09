package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The View for when the user is on the trade screen.
 */
public class BuyView extends JPanel implements PropertyChangeListener {

    private final String viewName = "buy";
    private final ExecuteBuyViewModel executeViewModel;
    private ExecuteBuyController executeBuyController;

    public BuyView(ExecuteBuyViewModel executeBuyViewModel) {
        this.executeBuyViewModel = executeBuyViewModel;
        this.executeBuyViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Buy Stocks Here");
        title.setAlighmentX(Component.CENTER_ALIGNMENT);


    }

    public String getViewName() {
        return viewName;
    }

}
