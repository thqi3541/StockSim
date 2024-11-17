package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();

        // Register and set up all panels
        appBuilder.addAuthenticationPanels()
                .addDashboardPanel("Guest", 0.0, 0.0)
                .addTradeSimulationPanel()
                .addDialogComponent()
                .addTransactionHistoryPanel();

        // Build the application frame and show LogInPanel by default
        JFrame application = appBuilder.build();
        application.setVisible(true);
    }
}