package utility.data_transfer;

public record StockDTO(
        String ticker,
        String company,
        String industry,
        double marketPrice
) {

}
