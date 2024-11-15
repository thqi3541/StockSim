package use_case.view_history;

public interface ViewHistoryOutputBoundary {

    void prepareSuccessView(ViewHistoryOutputData outputData);

    void prepareValidationExceptionView();
}
