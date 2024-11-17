package data_access;

import entity.Stock;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import utility.StockConfigService;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StockDataAccessObject implements StockDataAccessInterface, AutoCloseable {
    private static final String BASE_URL = "https://finnhub.io/api/v1";
    private static final long MANDATORY_API_DELAY_MS = 100;  // Minimum delay between API calls
    private static final int MAX_API_CALLS_PER_MINUTE = 60;  // Increased from 30 to match Finnhub's limit
    private static final long CACHE_DURATION_MS = 300000; // 5 minutes
    private final OkHttpClient client;
    private final String apiKey;
    private final Map<String, Stock> cache;
    private final ExecutorService apiExecutor;
    private final ScheduledExecutorService rateLimiter;
    private final AtomicInteger apiCallsInLastMinute;
    private final Object apiLock = new Object();

    public StockDataAccessObject() {
        // Initialize HTTP client with optimized settings
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        // Load API key from .env.local file
        try {
            Dotenv dotenv = Dotenv.configure()
                    .filename(".env.local")
                    .directory("/Users/ivork/GitHub/StockSim")
                    .load();
            this.apiKey = dotenv.get("STOCK_API_KEY");
            if (this.apiKey == null || this.apiKey.isEmpty()) {
                throw new RuntimeException("STOCK_API_KEY not found in .env.local file");
            }
            System.out.println("Successfully loaded API key from .env.local");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load API key from .env.local: " + e.getMessage(), e);
        }

        // Initialize cache and rate limiter
        this.cache = new ConcurrentHashMap<>();
        this.apiCallsInLastMinute = new AtomicInteger(0);

        // Create executors
        this.apiExecutor = Executors.newFixedThreadPool(5);
        this.rateLimiter = Executors.newSingleThreadScheduledExecutor();

        // Reset API call counter every minute
        rateLimiter.scheduleAtFixedRate(
                () -> apiCallsInLastMinute.set(0),
                1, 1, TimeUnit.MINUTES
        );
    }

    private void waitForRateLimit() throws InterruptedException {
        synchronized (apiLock) {
            // Mandatory delay between API calls for stability
            Thread.sleep(MANDATORY_API_DELAY_MS);

            // Check rate limit
            while (apiCallsInLastMinute.get() >= MAX_API_CALLS_PER_MINUTE) {
                System.out.println("Rate limit reached, waiting for next minute...");
                Thread.sleep(1000);
            }
            apiCallsInLastMinute.incrementAndGet();
        }
    }

    private CompletableFuture<JSONObject> makeApiCall(String url, int retries) {
        return CompletableFuture.supplyAsync(() -> {
            for (int attempt = 0; attempt <= retries; attempt++) {
                try {
                    waitForRateLimit(); // This now ensures 100ms between calls
                    System.out.println("Making API call to: " + url);
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("X-Finnhub-Token", apiKey)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful() && response.body() != null) {
                            return new JSONObject(response.body().string());
                        }
                        if (response.code() == 429) { // Too Many Requests
                            System.out.println("Rate limit hit, backing off...");
                            Thread.sleep(1000L * (attempt + 1)); // Reduced backoff time
                            continue;
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("API call interrupted", e);
                } catch (Exception e) {
                    if (attempt == retries) {
                        throw new RuntimeException("API call failed after " + retries + " retries", e);
                    }
                    try {
                        Thread.sleep(1000L * (attempt + 1)); // Reduced backoff time
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Sleep interrupted during retry", ie);
                    }
                }
            }
            throw new RuntimeException("API call failed after all retries");
        }, apiExecutor);
    }

    private Stock fetchStockData(String ticker) {
        // Check cache first
        Stock cachedStock = cache.get(ticker);
        if (cachedStock != null) {
            return cachedStock;
        }

        try {
            String quoteUrl = String.format("%s/quote?symbol=%s&token=%s", BASE_URL, ticker, apiKey);
            String profileUrl = String.format("%s/stock/profile2?symbol=%s&token=%s", BASE_URL, ticker, apiKey);

            // Make API calls concurrently
            CompletableFuture<JSONObject> quoteFuture = makeApiCall(quoteUrl, 3);
            CompletableFuture<JSONObject> profileFuture = makeApiCall(profileUrl, 3);

            // Wait for both API calls to complete
            CompletableFuture.allOf(quoteFuture, profileFuture).join();

            // Extract data from responses
            JSONObject quoteData = quoteFuture.get();
            JSONObject profileData = profileFuture.get();

            double price = quoteData.getDouble("c");
            String company = profileData.optString("name", "Unknown Company Name");
            String industry = profileData.optString("finnhubIndustry", "Unknown Industry");

            Stock stock = new Stock(ticker, company, industry, price);
            cache.put(ticker, stock);
            return stock;

        } catch (Exception e) {
            // If API calls fail, return cached data if available
            if (cachedStock != null) {
                return cachedStock;
            }
            throw new RuntimeException("Failed to fetch stock data for " + ticker, e);
        }
    }

    public Map<String, Stock> getStocks() {
        Map<String, Stock> stocks = new ConcurrentHashMap<>();

        try {
            System.out.println("Starting to fetch stock data sequentially...");

            for (String ticker : StockConfigService.Instance().getConfiguredTickers()) {
                try {
                    System.out.println("Fetching data for ticker: " + ticker);
                    Stock stock = fetchStockData(ticker);
                    if (stock != null) {
                        stocks.put(ticker, stock);
                        System.out.println("Successfully fetched data for " + ticker);
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching data for " + ticker + ": " + e.getMessage());
                }
                // Wait for rate limit after each successful fetch
                waitForRateLimit();
            }

            System.out.println("Completed fetching data for all stocks");
            return stocks;
        } catch (Exception e) {
            System.err.println("Error during stock data fetch: " + e.getMessage());
            return stocks;
        }
    }

    public void shutdown() {
        apiExecutor.shutdown();
        rateLimiter.shutdown();
        try {
            if (!apiExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                apiExecutor.shutdownNow();
            }
            if (!rateLimiter.awaitTermination(60, TimeUnit.SECONDS)) {
                rateLimiter.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        System.out.println("Closing StockDataAccessObject resources...");
        if (client != null) {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        }
    }
}
