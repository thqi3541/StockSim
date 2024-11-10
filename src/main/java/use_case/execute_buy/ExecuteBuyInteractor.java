package use_case.execute_buy;

import entity.Stock;
import entity.StockMarket;
import entity.Transaction;
import entity.User;
import session.SessionManager;

import java.util.Date;

/**
 * The Execute Buy Interactor.
 */
public class ExecuteBuyInteractor implements ExecuteBuyInputBoundary {

    private final ExecuteBuyDataAccess dataAccess;
    private final ExecuteBuyOutputBoundary outputPresenter;

    public ExecuteBuyInteractor(ExecuteBuyDataAccess dataAccess, ExecuteBuyOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    @Override
    public void execute(ExecuteBuyInputData data) {
        // get username by credential
        try {
            String username = SessionManager.Instance()
                    .getUsername(data.credential())
                    .orElseThrow(ValidationException::new);
            User currentUser = dataAccess.getUser(username);
            String ticker = data.ticker();
            int quantity = data.quantity();

            Stock stock = StockMarket.Instance().getStock(ticker).orElseThrow(StockNotFoundException::new);
            if (currentUser.getBalance() >= stock.getPrice() * quantity) {
                // then the transaction is valid
                Date timestamp = new Date();
                Transaction transaction = new Transaction(timestamp, ticker, quantity, stock.getPrice(), "buy");
                // call portfolio to add the transaction
                currentUser.getPortfolio().addTransaction(transaction);
                final ExecuteBuyOutputData outputData = new ExecuteBuyOutputData(
                        currentUser.getBalance(),
                        currentUser.getPortfolio()
                );
                outputPresenter.prepareSuccessView(outputData);
            } else {
                throw new InsufficientFundsException();
            }
        } catch (ValidationException e) {
            outputPresenter.prepareValidationErrorView();
        } catch (InsufficientFundsException e) {
            outputPresenter.prepareInsufficientFundsView();
        } catch (StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundView();
        }
    }

    // custom exceptions
    static class InsufficientFundsException extends Exception {}

    static class StockNotFoundException extends Exception {}

    static class ValidationException extends Exception {}
}
