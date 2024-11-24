package entity;

/**
 * A stock entity
 */
public class Stock {

    private final String ticker;
    private final String company;
    private final String industry;
    private double marketPrice;

    public Stock(String ticker, String company, String industry, double marketPrice) {
        this.ticker = ticker;
        this.company = company;
        this.industry = industry;
        this.marketPrice = marketPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompany() {
        return company;
    }

    public String getIndustry() {
        return industry;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    /**
     * Update the current market price of the stock
     * This method will be called every two minutes. (interval)
     *
     * @param price the new market price
     */
    public void updatePrice(double price) {
        this.marketPrice = price;
    }
}
