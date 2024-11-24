package interface_adapter.login;

import entity.User;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import utility.ServiceManager;
import view.ViewManager;
import view.view_events.*;

public class LoginPresenter implements LoginOutputBoundary {

    public LoginPresenter() {
        ServiceManager.Instance().registerService(LoginOutputBoundary.class, this);
    }

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

        // Switch to dashboard - stock data will be updated by MarketTracker automatically
        ViewManager.Instance().broadcastEvent(
                new SwitchPanelEvent("DashboardPanel")
        );
    }

    @Override
    public void prepareValidationExceptionView() {
        ViewManager.Instance().broadcastEvent(
                new DialogEvent("Sorry", "Please try again.")
        );
    }
}
