package use_case.execute_sell;

public interface ExecuteSellOutputBoundary {

    /**
     * Prepare the view for a successful sell.
     * @param outputData The output data from the sell.
     */
    void prepareSuccessView(ExecuteSellOutputData outputData);

    /**
     * Prepare the view for an insufficient margin call.
     */
    void prepareInsufficientMarginCallExceptionView();

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
     * @param s The error message.
     */
    void prepareInvalidQuantityExceptionView();

    /**
     * Prepare the view for a server exception.
     */
    void prepareServerErrorView();
}
