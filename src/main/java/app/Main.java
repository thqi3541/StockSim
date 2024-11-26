package app;

import javax.swing.JFrame;

public class Main {

  public static void main(String[] args) {
    // Instantiate the AppBuilder
    AppBuilder appBuilder = new AppBuilder();

    // Configure the application by adding various panels and components
    appBuilder
        .withTitle("StockSim")
        .withDimensions(1200, 900)
        .withInitialPanel("LogInPanel")
        .addAllPanels()
        .addDialogComponent();

    // Build the application frame
    JFrame application = appBuilder.build();

    // Make the application visible
    application.setVisible(true);
  }
}
