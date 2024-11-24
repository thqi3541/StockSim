package utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final String MARKET_TRACKER_CONFIG = "config/market-tracker-config.txt";

    private static final Properties marketTrackerProperties = new Properties();

    private static void loadConfig(String configFile) {
        try (InputStream input = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream(configFile)
        ) {
            if (input == null) {
                throw new RuntimeException("Unable to find configuration file: " + configFile);
            }
            marketTrackerProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file", e);
        }
    }

    static {
        loadConfig(MARKET_TRACKER_CONFIG);
    }

    public static String getMarketTrackerProperty(String key) {
        return marketTrackerProperties.getProperty(key);
    }
}