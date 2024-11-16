package entity;

/**
 * A stock entity
 */
public class Stock {

    // TODO: need to add more fields like company name, sector, etc.
    private final String ticker;
    private final String company;
    private final String industry;
    private double price;

    public Stock(String ticker, String company, String industry, double price) {
        this.ticker = ticker;
        this.company = company;
        this.industry = industry;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    /**
     * Update the current market price of the stock
     *
     * @param price the new price
     */
    public void updatePrice(double price) {
        this.price = price;
    }
}
