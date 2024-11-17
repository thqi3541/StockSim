package view.view_events;

import java.util.EnumSet;
import java.util.List;

import entity.Stock;

public class UpdateStockEvent extends ViewEvent {

    private final List<Stock> stocks;

    public UpdateStockEvent(List<Stock> stocks) {
        super(EnumSet.of(EventType.UPDATE_STOCK));
        this.stocks = stocks;
    }

    public List<Stock> getStocks() { return stocks; }
}
