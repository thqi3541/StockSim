package interface_adapter.view_history;

import use_case.view_history.ViewHistoryOutputBoundary;
import use_case.view_history.ViewHistoryOutputData;
import utility.ViewManager;
import view.view_events.ViewHistoryEvent;

public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {
    @Override
    public void prepareSuccessView(ViewHistoryOutputData outputData) {
        ViewManager.Instance().broadcastEvent(
                new ViewHistoryEvent(
                        outputData.transactionHistory()
                )
        );
    }

    @Override
    public void prepareValidationExceptionView() {
    }
}
