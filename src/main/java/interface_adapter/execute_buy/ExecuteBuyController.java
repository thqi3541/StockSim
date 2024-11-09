package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyInputBoundary;
import use_case.execute_buy.ExecuteBuyInputData;

public class ExecuteBuyController {

    private final ExecuteBuyInputBoundary interactor;

    public ExecuteBuyController(ExecuteBuyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String ticker, String quantity) {
        interactor.execute(new ExecuteBuyInputData(ticker, Integer.parseInt(quantity)));
    }
}
