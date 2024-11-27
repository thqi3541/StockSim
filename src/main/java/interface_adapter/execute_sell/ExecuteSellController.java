package interface_adapter.execute_sell;

import use_case.execute_sell.ExecuteSellInputBoundary;
import use_case.execute_sell.ExecuteSellInputData;
import utility.ClientSessionManager;
import utility.ServiceManager;

public class ExecuteSellController {

    private final ExecuteSellInputBoundary interactor;

    public ExecuteSellController(ExecuteSellInputBoundary interactor) {
        this.interactor = interactor;
        ServiceManager.Instance().registerService(ExecuteSellController.class, this);
    }

    public void execute(String ticker, String quantity) {
        try {
            final ExecuteSellInputData data = new ExecuteSellInputData(
                    ClientSessionManager.Instance().getCredential(),
                    ticker,
                    Integer.parseInt(quantity));

            interactor.execute(data);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity");
        }
    }
}
