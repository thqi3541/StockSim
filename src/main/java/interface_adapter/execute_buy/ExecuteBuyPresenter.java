package interface_adapter.execute_buy;

public class ExecuteBuyPresenter {
    public void present(ExecuteBuyOutputData outputData) {
        System.out.println("Buy executed: " + outputData.getStockName() + " " + outputData.getStockPrice() + " " + outputData.getStockQuantity());
    }
}
