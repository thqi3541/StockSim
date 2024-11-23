package use_case.login;

import entity.Stock;
import entity.User;

import java.util.List;

public record LoginOutputData(
        User user,
        List<Stock> stocks
) {
}
