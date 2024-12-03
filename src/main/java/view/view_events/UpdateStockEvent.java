package view.view_events;

import entity.Stock;
import java.util.List;

public class UpdateStockEvent extends ViewEvent {

    private final List<Stock> stocks;

    public UpdateStockEvent(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}
