package interface_adapter.login;

import entity.StockMarket;
import entity.User;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import utility.ViewManager;
import view.view_events.UpdateAssetEvent;
import view.view_events.UpdateStockEvent;
import view.view_events.UpdateUsernameEvent;

public class LoginPresenter implements LoginOutputBoundary {

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        User user = outputData.user();
        // update user asset data
        ViewManager.Instance().broadcastEvent(
                new UpdateAssetEvent(
                        user.getPortfolio(),
                        user.getBalance()
                )
        );
        // update stock market data
        ViewManager.Instance().broadcastEvent(
                new UpdateStockEvent(StockMarket.Instance().getStocks())
        );
        // update username data
        ViewManager.Instance().broadcastEvent(
                new UpdateUsernameEvent(user.getUsername())
        );
    }

    @Override
    public void prepareValidationExceptionView() {

    }
}
