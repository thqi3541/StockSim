package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton utility class for managing stock configuration and ticker loading.
 */
public class StockConfigService {
    private static volatile StockConfigService instance;
    private final List<String> configuredTickers;

    private StockConfigService() {
        this.configuredTickers = loadTickers();
    }

    public static StockConfigService Instance() {
        if (instance == null) {
            synchronized (StockConfigService.class) {
                if (instance == null) {
                    instance = new StockConfigService();
                }
            }
        }
        return instance;
    }

    public List<String> getConfiguredTickers() {
        return new ArrayList<>(configuredTickers);
    }

    private List<String> loadTickers() {
        List<String> tickers = new ArrayList<>();
        try {
            // First try to load from resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/tickers.txt");
            System.out.println("Resource stream from classloader: " + (inputStream != null ? "found" : "not found"));

            // If not found in resources, try to load from file system
            if (inputStream == null) {
                Path tickersPath = Paths.get("src", "main", "config", "tickers.txt");
                System.out.println("Looking for ticker file at: " + tickersPath.toAbsolutePath());
                if (!Files.exists(tickersPath)) {
                    throw new java.io.FileNotFoundException("Ticker file not found at: " + tickersPath);
                }
                inputStream = Files.newInputStream(tickersPath);
                System.out.println("Successfully opened ticker file from filesystem");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String ticker = line.trim();
                    if (!ticker.isEmpty()) {
                        tickers.add(ticker);
                    }
                }
            }
            System.out.println("Loaded " + tickers.size() + " tickers from configuration");
        } catch (IOException e) {
            System.err.println("Failed to read ticker file: " + e.getMessage());
            e.printStackTrace();
        }
        return tickers;
    }
}
