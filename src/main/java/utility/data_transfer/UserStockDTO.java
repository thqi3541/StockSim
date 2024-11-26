package utility.data_transfer;

public record UserStockDTO(
    StockDTO stock,
    double avgCost,
    int quantity
) {

}
