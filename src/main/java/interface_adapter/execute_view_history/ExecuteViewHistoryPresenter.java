package interface_adapter.execute_view_history;

import use_case.execute_view_history.ExecuteViewHistoryOutputBoundary;
import use_case.execute_view_history.ExecuteViewHistoryOutputData;
import utility.ViewManager;
import view.view_events.UpdateTransactionHistoryEvent;

/**
 * Presenter for the ViewHistory Use Case
 */
public class ExecuteViewHistoryPresenter implements ExecuteViewHistoryOutputBoundary {

    /**
     * Prepares the success view of the ViewHistory use case
     *
     * @param outputData the required display output data
     */
    @Override
    public void prepareSuccessView(ExecuteViewHistoryOutputData outputData) {
        ViewManager.Instance().broadcastEvent(
                new UpdateTransactionHistoryEvent(
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
