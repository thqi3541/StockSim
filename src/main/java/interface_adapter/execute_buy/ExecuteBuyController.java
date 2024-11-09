package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyClientInputData;
import use_case.execute_buy.ExecuteBuyInputBoundary;
import use_case.execute_buy.ExecuteBuyInputData;
import use_case.session.SessionManager;

public class ExecuteBuyController {

    private final ExecuteBuyInputBoundary interactor;

    public ExecuteBuyController(ExecuteBuyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(ExecuteBuyInputData data) {
        interactor.execute(data);
    }
}
