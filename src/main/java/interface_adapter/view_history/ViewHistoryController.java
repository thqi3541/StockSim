package interface_adapter.view_history;

import use_case.view_history.ViewHistoryInputBoundary;
import use_case.view_history.ViewHistoryInputData;
import utility.ClientSessionManager;

/**
 * Controller for the ViewHistory Use Case.
 */
public class ViewHistoryController {

    private final ViewHistoryInputBoundary interactor;

    public ViewHistoryController(ViewHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the ViewHistory Use Case.
     */
    public void execute() {
        final ViewHistoryInputData data = new ViewHistoryInputData(
                ClientSessionManager.Instance().getCredential());

        interactor.execute(data);
    }
}
