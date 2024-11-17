package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyOutputBoundary;
import use_case.execute_buy.ExecuteBuyOutputData;
import utility.ViewManager;
import view.view_events.DialogEvent;
import view.view_events.UpdateAssetEvent;
import view.view_events.UpdateTransactionHistoryEvent;

public class ExecuteBuyPresenter implements ExecuteBuyOutputBoundary {

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
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You have insufficient balance to buy this stock."));
    }

    @Override
    public void prepareStockNotFoundExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "The stock you are trying to buy does not exist."));
    }

    @Override
    public void prepareValidationExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You are not authorized to do this."));
    }
}
