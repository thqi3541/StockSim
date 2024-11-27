package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyOutputBoundary;
import use_case.execute_buy.ExecuteBuyOutputData;
import utility.ServiceManager;
import view.ViewManager;
import view.view_events.DialogEvent;
import view.view_events.UpdateAssetEvent;
import view.view_events.UpdateTransactionHistoryEvent;

import javax.swing.text.View;

public class ExecuteBuyPresenter implements ExecuteBuyOutputBoundary {

    public ExecuteBuyPresenter() {
        ServiceManager.Instance().registerService(ExecuteBuyOutputBoundary.class, this);
    }

    @Override
    public void prepareSuccessView(ExecuteBuyOutputData outputData) {
        ViewManager.Instance().broadcastEvent(
                new UpdateAssetEvent(
                        outputData.newPortfolio(),
                        outputData.newBalance()
                )
        );
        ViewManager.Instance().broadcastEvent(
                new UpdateTransactionHistoryEvent(outputData.newTransactionHistory())
        );
    }

    @Override
    public void prepareInsufficientBalanceExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You don't have sufficient cash balance to buy this stock."));
    }

    @Override
    public void prepareStockNotFoundExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "The stock you are trying to buy does not exist."));
    }

    @Override
    public void prepareValidationExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You are not authorized to do this."));
    }

    @Override
    public void prepareInvalidQuantityExceptionView(String errorMessage) {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", errorMessage));
    }

    @Override
    public void prepareServerErrorView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "Server Internal Error."));
    }
}
