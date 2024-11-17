package interface_adapter.login;

import entity.User;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import utility.ViewManager;
import view.view_events.*;

public class LoginPresenter implements LoginOutputBoundary {

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        User user = outputData.user();

        // Update username data
        ViewManager.Instance().broadcastEvent(
                new UpdateUsernameEvent(user.getUsername())
        );

        // Update user asset data
        ViewManager.Instance().broadcastEvent(
                new UpdateAssetEvent(
                        user.getPortfolio(),
                        user.getBalance()
                )
        );

        // Update history data
        ViewManager.Instance().broadcastEvent(
                new UpdateTransactionHistoryEvent(user.getTransactionHistory())
        );

        // Switch to dashboard - stock data will be updated by StockMarket automatically
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

    @Override
    public void prepareFailView(String error) {
        ViewManager.Instance().broadcastEvent(
                new DialogEvent("Error", error)
        );
    }
}
