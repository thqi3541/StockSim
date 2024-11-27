package data_transfer;

import java.util.Date;

public record TransactionDTO(
        Date timestamp,
        String ticker,
        int quantity,
        double executionPrice,
        String type
) {

}
