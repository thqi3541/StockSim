package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyInputBoundary;
import use_case.execute_buy.ExecuteBuyInputData;
import utility.ClientSessionManager;
import utility.ServiceManager;
import view.ViewManager;
import view.view_events.DialogEvent;

public class ExecuteBuyController {

    private final ExecuteBuyInputBoundary interactor;

    public ExecuteBuyController(ExecuteBuyInputBoundary interactor) {
        this.interactor = interactor;
        ServiceManager.Instance().registerService(ExecuteBuyController.class, this);
    }

    public void execute(String ticker, String quantity) {
        try {
            final ExecuteBuyInputData data = new ExecuteBuyInputData(
                    ClientSessionManager.Instance().getCredential(), ticker, Integer.parseInt(quantity));

            interactor.execute(data);
        } catch (NumberFormatException e) {
            ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "Invalid quantity"));
        }
    }
}
