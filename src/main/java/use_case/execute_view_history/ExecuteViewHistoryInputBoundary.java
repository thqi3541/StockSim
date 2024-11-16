package use_case.execute_view_history;

/**
 * Input Boundary for actions which are related to viewing the transaction history.
 */
public interface ExecuteViewHistoryInputBoundary {

    /**
     * Executes the view transaction history use case.
     *
     * @param input the input data
     */
    void execute(ExecuteViewHistoryInputData input);
}
