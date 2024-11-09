package use_case.execute_buy;

public interface ExecuteBuyOutputBoundary {

    void prepareSuccessView();
    void prepareInsufficientFundsView();
    void prepareInvalidInputView();
    void prepareInsufficientStocksView();
}
