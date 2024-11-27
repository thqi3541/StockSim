package data_transfer;

import java.util.Map;

public record PortfolioDTO(Map<String, UserStockDTO> userStocks) {}
