package use_case.execute_buy;

import entity.Stock;
import entity.StockMarket;
import entity.Transaction;
import entity.User;
import use_case.session.SessionService;

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
        User currentUser = dataAccess.getUser("user name");
        String ticker = data.ticker();
        int quantity = data.quantity();

        try {
            Stock stock = StockMarket.Instance().getStock(ticker).orElseThrow(StockNotFoundException::new);
            if (currentUser.getBalance() >= stock.getPrice() * quantity) {
                // then the transaction is valid
                Date timestamp = new Date();
                Transaction transaction = new Transaction(timestamp, ticker, quantity, stock.getPrice());
                // call portfolio to add the transaction
                currentUser.getPortfolio().addTransaction(transaction);
                // TODO: return some fancy output data
                final ExecuteBuyOutputData outputData = new ExecuteBuyOutputData("wow.");
                outputPresenter.prepareSuccessView(outputData);
            } else {
                throw new InsufficientFundsException();
            }
        } catch (InsufficientFundsException e) {
            outputPresenter.prepareInsufficientFundsView();
        } catch (StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundView();
        }
    }

    // custom exceptions
    static class InsufficientFundsException extends Exception {}

    static class StockNotFoundException extends Exception {}
}
