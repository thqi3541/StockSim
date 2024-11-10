package view.GUI;

import javax.swing.*;
import java.awt.*;

public class TradeSimulationFrame extends JFrame {
    private JLabel tradeSimulationLabel;

    public TradeSimulationFrame() {
        setTitle("Trade Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLayout(new BorderLayout(10, 10));
        setSize(1000, 800);

        JPanel mainPanel = new JPanel();  // 创建一个 JPanel 作为主容器
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));  // 设置布局为垂直 BoxLayout

        // 设置标题
        JLabel titleLabel = new JLabel("Trade Simulation", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));  // 设置字体为Arial，加粗，24号
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));  // 设置边距

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
