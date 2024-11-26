package data_access;

import entity.Stock;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import utility.exceptions.RateLimitExceededException;

/**
 * A DataAccessObject that retrieves real time stock data
 */
public class StockDataAccessObject implements StockDataAccessInterface {

  private static final String BASE_URL = "https://finnhub.io/api/v1";
  private static final String TICKERS_FILE = "/config/tickers.txt";
  // Rate limit exceeded error code provided by Finnhub documentation
  private static final int LIMIT_EXCEED_ERROR_CODE = 429;

  private final OkHttpClient client;
  private final String apiKey;
  private final List<String> tickers;

  public StockDataAccessObject() {
    this.client = new OkHttpClient();
    this.tickers = new ArrayList<>();

    // Load .env.local file and get API token
    Dotenv dotenv = Dotenv.configure().filename(".env.local").load();
    this.apiKey = dotenv.get("STOCK_API_KEY");

    // Fetch ticker data from resource file and store in list
    try (InputStream inputStream = getClass().getResourceAsStream(TICKERS_FILE)) {
      if (inputStream == null) {
        throw new RuntimeException("Unable to find configuration file: " + TICKERS_FILE);
      } else {
        // Reads content in config/tickers text file
        try (Scanner scanner = new Scanner(inputStream)) {
          while (scanner.hasNextLine()) {
            String ticker = scanner.nextLine().trim();
            // Stores ticker in list containing all tickers
            tickers.add(ticker);
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Error loading configuration file", e);
    }

  }


  /**
   * Get all stock information
   *
   * @return a hashmap with the stock ticker as the key and the Stock entity as the value. It should
   * contain all stocks in the database.
   */
  @Override
  public Map<String, Stock> getStocks() throws RateLimitExceededException {
    HashMap<String, Stock> stocks = new HashMap<>();

    for (String ticker : tickers) {
      // Information to create a new default stock
      String company = "Unknown Company Name";
      String industry = "Unknown Industry";

      // Retrieves ticker current market price
      double price = getMarketPrice(ticker);

      // Profile2 api call to get company name and industry
      try {
        // Creates new request with Profile2 url
        String profileUrl = String.format("%s/stock/profile2?symbol=%s&token=%s", BASE_URL, ticker,
            apiKey);
        Request profileRequest = new Request.Builder().url(profileUrl).build();

        // Initiates API call request
        try (Response profileResponse = client.newCall(profileRequest).execute()) {
          if (profileResponse.isSuccessful()) {
            JSONObject jsonObject = new JSONObject(profileResponse.body().string());
            // Gets the ticker company "name" or returns default company name if unavailable
            company = jsonObject.optString("name", company);
            // Gets the company's industry "finnhubIndustry" based on finnhub's classification or returns default industry if unavailable
            industry = jsonObject.optString("finnhubIndustry", industry);
          } else if (profileResponse.code() == LIMIT_EXCEED_ERROR_CODE) {
            throw new RateLimitExceededException();
          } else {
            // Error message for errors other than exceed rate limit
            System.out.println("Failed to fetch profile data for ticker: " + ticker);
          }
        }

        // Check if the stock already exists in the map
        if (stocks.containsKey(ticker)) {
          // Update the price of the existing stock
          stocks.get(ticker).updatePrice(price);
        } else {
          // Create a new Stock object and add it to the map
          Stock stock = new Stock(ticker, company, industry, price);
          stocks.put(ticker, stock);
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Return an unmodifiable view of the stocks map to prevent external modifications
    return Collections.unmodifiableMap(stocks);
  }

  /**
   * Get updated market price for all stocks
   *
   * @return a hashmap with the stock ticker as the key and the updated market price as the value.
   */
  @Override
  public Map<String, Double> getUpdatedPrices() throws RateLimitExceededException {
    HashMap<String, Double> updatedPrices = new HashMap<>();

    // Calls helper function to get current market price for each ticker and stores it in updatedPrice
    for (String ticker : tickers) {
      updatedPrices.put(ticker, getMarketPrice(ticker));
    }

    // Return an unmodifiable view of the updated prices map to prevent external modifications
    return Collections.unmodifiableMap(updatedPrices);
  }

  /**
   * Helper function to retrieve the current market price of the given ticker
   *
   * @param ticker the stock ticker
   * @return the updated price
   */
  private double getMarketPrice(String ticker) throws RateLimitExceededException {

    // Quote api call to get current market price
    try {
      // Creates new request with Quote url
      String quoteUrl = String.format("%s/quote?symbol=%s&token=%s", BASE_URL, ticker, apiKey);
      Request quoteRequest = new Request.Builder().url(quoteUrl).build();

      // Initiates API call request
      try (Response quoteResponse = client.newCall(quoteRequest).execute()) {
        if (quoteResponse.isSuccessful()) {
          JSONObject jsonObject = new JSONObject(quoteResponse.body().string());
          // Gets the current market price "c"
          return jsonObject.getDouble("c");
        } else if (quoteResponse.code() == LIMIT_EXCEED_ERROR_CODE) {
          throw new RateLimitExceededException();
        } else {
          // Error message for errors other than exceed rate limit
          System.out.println("Failed to fetch quote data for ticker: " + ticker);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Returns default price if call failed
    return 0.0;
  }
}
