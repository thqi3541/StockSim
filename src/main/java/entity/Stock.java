package entity;

/**
 * A stock entity representing a publicly traded stock.
 */
public class Stock {

    private final String ticker;
    private final String company;
    private final String industry;
    private double marketPrice;

    /**
     * Constructs a new Stock instance.
     *
     * @param ticker      the stock ticker symbol
     * @param company     the company name
     * @param industry    the industry the company belongs to
     * @param marketPrice the current market price of the stock
     * @throws IllegalArgumentException if ticker, company, or industry is null/empty or if marketPrice is negative
     */
    public Stock(String ticker, String company, String industry, double marketPrice) {
        if (ticker == null || ticker.isEmpty()) {
            throw new IllegalArgumentException("Ticker cannot be null or empty.");
        }
        if (company == null || company.isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty.");
        }
        if (industry == null || industry.isEmpty()) {
            throw new IllegalArgumentException("Industry cannot be null or empty.");
        }
        if (marketPrice < 0) {
            throw new IllegalArgumentException("Market price cannot be negative.");
        }

        this.ticker = ticker;
        this.company = company;
        this.industry = industry;
        this.marketPrice = marketPrice;
    }

    /**
     * Gets the stock ticker symbol.
     *
     * @return the ticker symbol
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * Gets the company name.
     *
     * @return the company name
     */
    public String getCompany() {
        return company;
    }

    /**
     * Gets the industry name.
     *
     * @return the industry name
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * Gets the current market price of the stock.
     *
     * @return the market price
     */
    public double getMarketPrice() {
        return marketPrice;
    }

    /**
     * Updates the current market price of the stock.
     *
     * @param price the new market price
     * @throws IllegalArgumentException if the price is negative
     */
    public void updatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.marketPrice = price;
    }

    @Override
    public String toString() {
        return String.format(
                "Stock[ticker=%s, company=%s, industry=%s, marketPrice=%.2f]",
                ticker, company, industry, marketPrice
        );
    }
}