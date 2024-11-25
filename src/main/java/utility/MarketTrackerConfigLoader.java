package utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MarketTrackerConfigLoader {

    private static final String MARKET_TRACKER_CONFIG = "config/market-tracker-config.txt";

    private static final Properties marketTrackerProperties = new Properties();

    private static void loadMarketTrackerConfig() {
        try (InputStream input = MarketTrackerConfigLoader.class
                .getClassLoader()
                .getResourceAsStream(MarketTrackerConfigLoader.MARKET_TRACKER_CONFIG)
        ) {
            if (input == null) {
                throw new RuntimeException("Unable to find configuration file: " + MarketTrackerConfigLoader.MARKET_TRACKER_CONFIG);
            }
            marketTrackerProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    static {
        loadMarketTrackerConfig();
    }

    public static String getMarketTrackerProperty(String key) {
        return marketTrackerProperties.getProperty(key);
    }
}