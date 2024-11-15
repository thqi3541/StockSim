package use_case.view_history;

import entity.User;
import use_case.execute_buy.ExecuteBuyDataAccessInterface;
import utility.exceptions.ValidationException;

/**
 * The View History Interactor.
 */
public class ViewHistoryInteractor implements ViewHistoryInputBoundary {

    private final ExecuteBuyDataAccessInterface dataAccess;
    private final ViewHistoryOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ViewHistoryInteractor class.
     * It instantiates a new ViewHistory Interactor.
     * @param dataAccess the data access
     * @param outputBoundary the output boundary
     */
    public ViewHistoryInteractor(ExecuteBuyDataAccessInterface dataAccess, ViewHistoryOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    /**
     * This methods executes view transaction history
     * @param data the input data
     */
    @Override
    public void execute(ViewHistoryInputData data) {
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());

            outputPresenter.prepareSuccessView(new ViewHistoryOutputData(
                    currentUser.getTransactionHistory()
            ));
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        }
    }
}
