package interface_adapter.view_history;

import use_case.view_history.ViewHistoryInputBoundary;
import use_case.view_history.ViewHistoryInputData;
import utility.ClientSessionManager;

public class ViewHistoryController {

    private final ViewHistoryInputBoundary interactor;

    public ViewHistoryController(ViewHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        final ViewHistoryInputData data = new ViewHistoryInputData(
                ClientSessionManager.Instance().getCredential());

        interactor.execute(data);
    }
}
