package use_case.execute_buy;

import entity.User;

public interface ExecuteBuyDataAccess {
    User getUser(String username);
}
