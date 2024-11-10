package use_case.execute_buy;

public interface ExecuteBuyOutputBoundary {

    void prepareSuccessView(ExecuteBuyOutputData outputData);

    void prepareInsufficientBalanceErrorView();

    void prepareStockNotFoundErrorView();

    void prepareValidationErrorView();
}
