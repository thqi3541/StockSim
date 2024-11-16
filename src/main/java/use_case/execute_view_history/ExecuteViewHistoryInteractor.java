package use_case.execute_view_history;

import entity.User;
import utility.exceptions.ValidationException;

/**
 * The View History Interactor.
 */
public class ExecuteViewHistoryInteractor implements ExecuteViewHistoryInputBoundary {

    private final ExecuteViewHistoryDataAccessInterface dataAccess;
    private final ExecuteViewHistoryOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ExecuteViewHistoryInteractor class.
     * It instantiates a new ViewHistory Interactor.
     *
     * @param dataAccess     the data access
     * @param outputBoundary the output boundary
     */
    public ExecuteViewHistoryInteractor(ExecuteViewHistoryDataAccessInterface dataAccess, ExecuteViewHistoryOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    /**
     * This method executes view transaction history
     *
     * @param data the input data
     */
    @Override
    public void execute(ExecuteViewHistoryInputData data) {
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());
            // Prepare output data to feed into presenter
            outputPresenter.prepareSuccessView(new ExecuteViewHistoryOutputData(
                    currentUser.getTransactionHistory()
            ));
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        }
    }
}
