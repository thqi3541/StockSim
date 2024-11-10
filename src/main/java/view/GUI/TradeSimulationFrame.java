package view.GUI;

import javax.swing.*;
import java.awt.*;

public class TradeSimulationFrame extends JFrame {
    private JLabel tradeSimulationLabel;

    public TradeSimulationFrame() {
        setTitle("Trade Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Trade Simulation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel upperLeftPanel = new JPanel(new BorderLayout());
        upperLeftPanel.add(new MarketSearchPanel(), BorderLayout.NORTH);
        upperLeftPanel.add(new TotalAssetsPanel(), BorderLayout.CENTER);

        JPanel upperPanel = new JPanel(new BorderLayout());
        upperPanel.add(upperLeftPanel, BorderLayout.WEST);
        upperPanel.add(new OrderEntryPanel(), BorderLayout.CENTER);

        mainPanel.add(titleLabel);
        mainPanel.add(upperPanel);
        mainPanel.add(new PortfolioPanel());

        getContentPane().add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new TradeSimulationFrame();
    }
}
