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

import interface_adapter.execute_buy.ExecuteBuyViewModel;
import interface_adapter.execute_buy.ExecuteBuyController;
/**
 * The View for when the user is on the trade screen.
 */
public class BuyView {

    private final String viewName = "buy";
    private ExecuteBuyController executeBuyController;

    final JLabel title = new JLabel("Buy Stocks Here");


}
