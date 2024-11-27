package interface_adapter.execute_sell;

import use_case.execute_sell.ExecuteSellInputBoundary;
import use_case.execute_sell.ExecuteSellInputData;
import utility.ClientSessionManager;
import utility.ServiceManager;
import validations.QuantityValidator;
import validations.TickerValidator;
import view.ViewManager;
import view.view_events.DialogEvent;

public class ExecuteSellController {

    private final ExecuteSellInputBoundary interactor;

    public ExecuteSellController(ExecuteSellInputBoundary interactor) {
        this.interactor = interactor;
        ServiceManager.Instance().registerService(ExecuteSellController.class, this);
    }

    public void execute(String ticker, String quantity) {
        if (!TickerValidator.isTickerValid(ticker)) {
            ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "Ticker is invalid"));
        }

        if (!QuantityValidator.isQuantityValid(quantity)) {
            ViewManager.Instance().broadcastEvent(new DialogEvent("Failed", "Quantity is invalid"));
        }

        final ExecuteSellInputData data = new ExecuteSellInputData(
                ClientSessionManager.Instance().getCredential(), ticker, Integer.parseInt(quantity));

        interactor.execute(data);
    }
}
