package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Instantiate the AppBuilder
        AppBuilder appBuilder = new AppBuilder();

        // Configure the application by adding various panels and components
        appBuilder
                .withTitle("Trading Simulator")
                .withDimensions(1200, 900)
                .withInitialPanel("LogInPanel")
                .addAuthenticationPanels()
                .addDashboardPanel()
                .addActionPanels()
                .addDialogComponent();

        // Build the application frame
        JFrame application = appBuilder.build();

        // Make the application visible
        application.setVisible(true);
    }
}
