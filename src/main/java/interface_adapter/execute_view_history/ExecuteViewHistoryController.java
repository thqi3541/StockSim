package interface_adapter.execute_view_history;

import use_case.execute_view_history.ExecuteViewHistoryInputBoundary;
import use_case.execute_view_history.ExecuteViewHistoryInputData;
import utility.ClientSessionManager;

/**
 * Controller for the ViewHistory Use Case.
 */
public class ExecuteViewHistoryController {

    private final ExecuteViewHistoryInputBoundary interactor;

    public ExecuteViewHistoryController(ExecuteViewHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the ViewHistory Use Case.
     */
    public void execute() {
        final ExecuteViewHistoryInputData data = new ExecuteViewHistoryInputData(
                ClientSessionManager.Instance().getCredential());

        interactor.execute(data);
    }
}
