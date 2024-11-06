package data_access;

import java.util.Map;

public interface IStockDataAccess {

    double getStockPrice(String ticker);

    Map<String, Double> getStockPrices();
}
