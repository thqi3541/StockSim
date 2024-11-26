package interface_adapter.execute_sell;

import use_case.execute_sell.ExecuteSellOutputBoundary;
import use_case.execute_sell.ExecuteSellOutputData;
import utility.ServiceManager;
import view.ViewManager;
import view.view_events.DialogEvent;
import view.view_events.UpdateAssetEvent;
import view.view_events.UpdateTransactionHistoryEvent;

public class ExecuteSellPresenter implements ExecuteSellOutputBoundary {

    public ExecuteSellPresenter() {
        ServiceManager.Instance().registerService(ExecuteSellOutputBoundary.class, this);
    }

    @Override
    public void prepareSuccessView(ExecuteSellOutputData outputData) {
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
    public void prepareInsufficientMarginCallExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You do not have enough margin call to sell this stock."));
    }

    @Override
    public void prepareStockNotFoundExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "The stock you are trying to sell does not exist."));
    }

    @Override
    public void prepareValidationExceptionView() {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "You are not authorized to do this."));
    }
}
