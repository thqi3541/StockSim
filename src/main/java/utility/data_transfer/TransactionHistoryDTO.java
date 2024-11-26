package utility.data_transfer;

import java.util.List;

public record TransactionHistoryDTO(
    List<TransactionDTO> transactions
) {

}
