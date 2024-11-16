package use_case.view_history;

/**
 * Input Boundary for actions which are related to viewing the transaction history.
 */
public interface ViewHistoryInputBoundary {

    /**
     * Executes the view transaction history use case.
     *
     * @param input the input data
     */
    void execute(ViewHistoryInputData input);
}
