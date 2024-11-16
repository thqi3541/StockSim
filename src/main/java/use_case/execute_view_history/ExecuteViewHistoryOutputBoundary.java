package use_case.execute_view_history;

/**
 * The output boundary for the ViewHistory use case.
 */
public interface ExecuteViewHistoryOutputBoundary {

    /**
     * Prepares the success view for the ViewHistory use case.
     *
     * @param outputData the output data
     */
    void prepareSuccessView(ExecuteViewHistoryOutputData outputData);

    /**
     * Prepares the ValidationException view for the ViewHistory use case
     */
    void prepareValidationExceptionView();
}
