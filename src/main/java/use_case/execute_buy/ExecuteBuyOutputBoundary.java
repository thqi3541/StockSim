package use_case.execute_buy;

public interface ExecuteBuyOutputBoundary {

    /**
     * Prepare the view for a successful buy.
     * @param outputData The output data from the buy.
     */
    void prepareSuccessView(ExecuteBuyOutputData outputData);

    /**
     * Prepare the view for an insufficient balance exception.
     */
    void prepareInsufficientBalanceExceptionView();

    /**
     * Prepare the view for a stock not found exception.
     */
    void prepareStockNotFoundExceptionView();

    /**
     * Prepare the view for a validation exception.
     */
    void prepareValidationExceptionView();

    /**
     * Prepare the view for an invalid quantity exception.
     */
    void prepareInvalidQuantityExceptionView();

    /**
     * Prepare the view for a server exception.
     */
    void prepareServerErrorView();
}
