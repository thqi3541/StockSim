package use_case.view_history;

/** The output boundary for the ViewHistory use case. */
public interface ViewHistoryOutputBoundary {

  /**
   * Prepares the success view for the ViewHistory use case.
   *
   * @param outputData the output data
   */
  void prepareSuccessView(ViewHistoryOutputData outputData);

  /** Prepares the ValidationException view for the ViewHistory use case */
  void prepareValidationExceptionView();
}
