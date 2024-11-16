package use_case.execute_view_history;

/**
 * This class records the input data for the ViewHistory use case
 *
 * @param credential the credential of the user
 */
public record ExecuteViewHistoryInputData(
        String credential
) {
}
