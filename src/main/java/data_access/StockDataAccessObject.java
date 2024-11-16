package data_access;

import entity.Stock;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StockDataAccessObject implements IStockDataAccess {
    private static final String BASE_URL = "https://finnhub.io/api/v1";
    private final OkHttpClient client;
    private final String apiKey;

    public StockDataAccessObject() {
        this.client = new OkHttpClient();

        // Load .env.local file and get the API token
        Dotenv dotenv = Dotenv.configure().filename(".env.local").load();
        this.apiKey = dotenv.get("STOCK_API_KEY");

    }

    public static void main(String[] args) {
        StockDataAccessObject stockDataAccessObject = new StockDataAccessObject();
        stockDataAccessObject.getStocks();
    }

    /**
     * Get the prices of all stocks
     *
     * @return a hashmap with the stock ticker as the key and the Stock entity as the value.
     * It should contain all stocks in the database.
     */
    @Override
    public Map<String, Stock> getStocks() {
        // TODO: implement api call limit exceeded exception/notification (Error 429)
        Map<String, Stock> stocks = new HashMap<>();

        try {
            // Reads content in config/tickers text file
            InputStream inputStream = getClass().getResourceAsStream("/tickers.txt");

            if (inputStream == null) {
                throw new FileNotFoundException("Ticker resource file not found.");
            }
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Information to create a new stock
                String ticker = line;
                String company = "Unknown Company Name";
                String industry = "Unknown Industry";
                double price = 0.0;

                // Url and request for Quote api call
                String quoteUrl = String.format("%s/quote?symbol=%s&token=%s", BASE_URL, ticker, apiKey);
                Request quoteRequest = new Request.Builder().url(quoteUrl).build();

                // Url and request for Profile2 api call
                String profileUrl = String.format("%s/stock/profile2?symbol=%s&token=%s", BASE_URL, ticker, apiKey);
                Request profileRequest = new Request.Builder().url(profileUrl).build();

                // Quote api call to get current market price
                try (Response quoteResponse = client.newCall(quoteRequest).execute()) {
                    if (quoteResponse.isSuccessful()) {
                        String quoteResponseBody = quoteResponse.body().string();
                        JSONObject jsonObject = new JSONObject(quoteResponseBody);
                        price = jsonObject.getDouble("c");
                    } else {
                        System.out.println("API call limit exceeded.");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Profile2 api call to get company name and industry
                try (Response profileResponse = client.newCall(profileRequest).execute()) {
                    if (profileResponse.isSuccessful()) {
                        String profileResponseBody = profileResponse.body().string();
                        JSONObject jsonObject = new JSONObject(profileResponseBody);
                        company = jsonObject.getString("name");
                        industry = jsonObject.getString("finnhubIndustry");
                    } else {
                        System.out.println("API call limit exceeded.");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Creates new Stock with retrieved information
                Stock stock = new Stock(ticker, company, industry, price);
                stocks.put(line, stock);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return stocks;
    }
}
