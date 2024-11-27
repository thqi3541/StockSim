package use_case.view_history;

import entity.User;
import utility.ServiceManager;
import utility.exceptions.ValidationException;

/**
 * The View History Interactor.
 */
public class ViewHistoryInteractor implements ViewHistoryInputBoundary {

    private final ViewHistoryDataAccessInterface dataAccess;
    private final ViewHistoryOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ViewHistoryInteractor class. It instantiates a new ViewHistory
     * Interactor.
     *
     * @param dataAccess     the data access
     * @param outputBoundary the output boundary
     */
    public ViewHistoryInteractor(
            ViewHistoryDataAccessInterface dataAccess,
            ViewHistoryOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
        ServiceManager.Instance()
                      .registerService(ViewHistoryInputBoundary.class, this);
    }

    /**
     * This method executes view transaction history
     *
     * @param data the input data
     */
    @Override
    public void execute(ViewHistoryInputData data) {
        try {
            // Get current user
            User currentUser =
                    dataAccess.getUserWithCredential(data.credential());
            // Prepare output data to feed into presenter
            outputPresenter.prepareSuccessView(
                    new ViewHistoryOutputData(
                            currentUser.getTransactionHistory()));
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        }
    }
}
