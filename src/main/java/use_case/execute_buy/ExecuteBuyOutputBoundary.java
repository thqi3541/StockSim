package use_case.execute_buy;

public interface ExecuteBuyOutputBoundary {

    void prepareSuccessView();

    void prepareSuccessView(ExecuteBuyOutputData outputData);

    void prepareInsufficientFundsView();

    void prepareStockNotFoundView();

    void prepareValidationErrorView();
}
