package data_access;

import entity.Stock;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import utility.ServiceManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class StockDataAccessObject implements StockDataAccessInterface {
    private static final String BASE_URL = "https://finnhub.io/api/v1";
    private final OkHttpClient client;
    private final String apiKey;
    private final Map<String, CompanyInfo> companyCache;
    private final List<String> tickers;

    public StockDataAccessObject() {
        // Configure OkHttpClient with timeouts and connection pool
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        // Load API key
        Dotenv dotenv = Dotenv.configure().filename(".env.local").load();
        this.apiKey = dotenv.get("STOCK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("STOCK_API_KEY not found in .env.local file");
        }

        // Initialize cache and load tickers
        this.companyCache = new ConcurrentHashMap<>();
        this.tickers = loadTickers();

        ServiceManager.Instance().registerService(StockDataAccessInterface.class, this);
    }

    @Override
    public Map<String, Stock> getStocks() throws IOException {
        return getStocks(null);
    }

    public Map<String, Stock> getStocks(Consumer<String> progressCallback) throws IOException {
        Map<String, Stock> stocks = new HashMap<>();

        for (int i = 0; i < tickers.size(); i++) {
            String ticker = tickers.get(i);
            try {
                if (progressCallback != null) {
                    progressCallback.accept(String.format("Fetching data for %s (%d/%d)",
                            ticker, i + 1, tickers.size()));
                }

                Stock stock = fetchStockData(ticker);
                stocks.put(ticker, stock);

                // Small delay between API calls
                if (i < tickers.size() - 1) {
                    Thread.sleep(100);
                }
            } catch (IOException e) {
                System.err.println("Failed to fetch data for " + ticker + ": " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Stock data fetch was interrupted", e);
            }
        }

        return stocks;
    }

    private List<String> loadTickers() {
        List<String> loadedTickers = new ArrayList<>();

        try {
            // Try resources first, then file system
            InputStream inputStream = getClass().getResourceAsStream("/config/tickers.txt");
            if (inputStream == null) {
                Path tickersPath = Paths.get("src", "main", "config", "tickers.txt");
                if (!Files.exists(tickersPath)) {
                    throw new IOException("tickers.txt not found");
                }
                inputStream = Files.newInputStream(tickersPath);
            }

            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    String ticker = scanner.nextLine().trim();
                    if (!ticker.isEmpty()) {
                        loadedTickers.add(ticker);
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load tickers: " + e.getMessage());
        }

        if (loadedTickers.isEmpty()) {
            throw new IllegalStateException("No tickers found in tickers.txt");
        }

        return Collections.unmodifiableList(loadedTickers);
    }

    private Stock fetchStockData(String ticker) throws IOException {
        // Get current price
        double price = fetchCurrentPrice(ticker);

        // Get or update company info
        CompanyInfo info = getCompanyInfo(ticker);

        return new Stock(ticker, info.name, info.industry, price);
    }

    private double fetchCurrentPrice(String ticker) throws IOException {
        String quoteUrl = String.format("%s/quote?symbol=%s&token=%s", BASE_URL, ticker, apiKey);
        Request request = new Request.Builder().url(quoteUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Quote API failed with code: " + response.code());
            }
            JSONObject quoteData = new JSONObject(response.body().string());
            return quoteData.getDouble("c");
        }
    }

    private CompanyInfo getCompanyInfo(String ticker) throws IOException {
        // Check cache first
        CompanyInfo cachedInfo = companyCache.get(ticker);
        if (cachedInfo != null && !cachedInfo.isExpired()) {
            return cachedInfo;
        }

        // Fetch new company info
        String profileUrl = String.format("%s/stock/profile2?symbol=%s&token=%s", BASE_URL, ticker, apiKey);
        Request request = new Request.Builder().url(profileUrl).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Profile API failed with code: " + response.code());
            }
            JSONObject profileData = new JSONObject(response.body().string());

            CompanyInfo info = new CompanyInfo(
                    profileData.optString("name", "Unknown Company Name"),
                    profileData.optString("finnhubIndustry", "Unknown Industry")
            );

            companyCache.put(ticker, info);
            return info;
        }
    }

    private static class CompanyInfo {
        final String name;
        final String industry;
        final long timestamp;

        CompanyInfo(String name, String industry) {
            this.name = name;
            this.industry = industry;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            // Company info cache expires after 24 hours
            return System.currentTimeMillis() - timestamp > TimeUnit.HOURS.toMillis(24);
        }
    }
}
