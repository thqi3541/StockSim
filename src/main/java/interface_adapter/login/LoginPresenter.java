package interface_adapter.login;

import data_access.StockDataAccessInterface;
import entity.Stock;
import entity.User;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import utility.ServiceManager;
import utility.ViewManager;
import view.view_events.*;

import java.util.ArrayList;
import java.util.List;

public class LoginPresenter implements LoginOutputBoundary {

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        User user = outputData.user();
        // update username data
        ViewManager.Instance().broadcastEvent(
                new UpdateUsernameEvent(user.getUsername())
        );
        // update user asset data
        ViewManager.Instance().broadcastEvent(
                new UpdateAssetEvent(
                        user.getPortfolio(),
                        user.getBalance()
                )
        );
        // update history data
        ViewManager.Instance().broadcastEvent(
                new UpdateTransactionHistoryEvent(user.getTransactionHistory())
        );
        // update stock data
        try {
            StockDataAccessInterface stockDAO = ServiceManager.Instance().getService(StockDataAccessInterface.class);
            if (stockDAO == null) {
                throw new RuntimeException("StockDataAccessInterface not registered.");
            }
            List<Stock> stockList = new ArrayList<>(stockDAO.getStocks().values());
            ViewManager.Instance().broadcastEvent(new UpdateStockEvent(stockList));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // switch to dashboard
        ViewManager.Instance().broadcastEvent(
                new SwitchPanelEvent("DashboardPanel")
        );
    }

    @Override
    public void prepareValidationExceptionView() {
        ViewManager.Instance().broadcastEvent(
                new DialogEvent("Sorry", "We cannot find your account. Please try again.")
        );
    }
}
