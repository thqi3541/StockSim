package interface_adapter.view_history;

import use_case.view_history.ViewHistoryOutputBoundary;
import use_case.view_history.ViewHistoryOutputData;
import utility.ViewManager;
import view.view_events.ViewHistoryEvent;

/**
 * Presenter for the ViewHistory Use Case
 */
public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {

    /**
     * Prepares the success view of the ViewHistory use case
     *
     * @param outputData the required display output data
     */
    @Override
    public void prepareSuccessView(ViewHistoryOutputData outputData) {
        ViewManager.Instance().broadcastEvent(
                new ViewHistoryEvent(
                        outputData.transactionHistory()
                )
        );
    }

    /**
     * Prepares the ValidationException view for the ViewHistory use case
     */
    @Override
    public void prepareValidationExceptionView() {
    }
}
