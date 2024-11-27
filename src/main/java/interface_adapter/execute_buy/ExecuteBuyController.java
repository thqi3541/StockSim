package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyInputBoundary;
import use_case.execute_buy.ExecuteBuyInputData;
import utility.ClientSessionManager;
import utility.ServiceManager;
import validations.QuantityValidator;
import validations.TickerValidator;
import view.ViewManager;
import view.view_events.DialogEvent;

public class ExecuteBuyController {

    private final ExecuteBuyInputBoundary interactor;

    public ExecuteBuyController(ExecuteBuyInputBoundary interactor) {
        this.interactor = interactor;
        ServiceManager.Instance()
                      .registerService(ExecuteBuyController.class, this);
    }

    public void execute(String ticker, String quantity) {
        if (!TickerValidator.isTickerValid(ticker)) {
            ViewManager.Instance().broadcastEvent(
                    new DialogEvent("Failed", "Ticker is invalid"));
        }

        if (!QuantityValidator.isQuantityValid(quantity)) {
            ViewManager.Instance().broadcastEvent(
                    new DialogEvent("Failed", "Quantity is invalid"));
        }

        final ExecuteBuyInputData data = new ExecuteBuyInputData(
                ClientSessionManager.Instance().getCredential(), ticker,
                Integer.parseInt(quantity));

        interactor.execute(data);
    }
}
