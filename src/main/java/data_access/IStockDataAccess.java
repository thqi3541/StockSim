package data_access;

import java.util.Map;

public interface IStockDataAccess {

    public double getStockPrice(String ticker);
    public Map<String, Double> getStockPrices();
}
