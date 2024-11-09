package interface_adapter.execute_buy;

public class ExecuteBuyController {

    private ExecuteBuyInputBoundary interactor;

    public ExecuteBuyController(ExecuteBuyInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String ticker, String quantity) {
        interactor.execute(new ExecuteBuyInputData(ticker, Integer.parseInt(quantity)));
    }
}
