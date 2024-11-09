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

    private final SessionService sessionService;
    private final ExecuteBuyDataAccess dataAccess;
    private final ExecuteBuyOutputBoundary outputPresenter;

    public ExecuteBuyInteractor(SessionService sessionService, ExecuteBuyDataAccess dataAccess, ExecuteBuyOutputBoundary outputBoundary) {
        this.sessionService = sessionService;
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    @Override
    public void execute(ExecuteBuyInputData data) {
        User currentUser = dataAccess.getUser(sessionService.getCurrentUsername());
        String ticker = data.ticker();
        int quantity = data.quantity();
        Stock stock = StockMarket.Instance().getStock(ticker).orElse(null);

        try {
            if (currentUser.getBalance() >= getTotalCost(ticker, quantity)) {
                Date timestamp = new Date();
                assert stock != null;
                Transaction transaction = new Transaction(timestamp, ticker, quantity, stock.getPrice());
                currentUser.getPortfolio().addTransaction(transaction);
                final ExecuteBuyOutputData outputData = new ExecuteBuyOutputData("wow.");
                outputPresenter.prepareSuccessView(outputData);
            } else {
                throw new InsufficientFundsException();
            }
        } catch (InvalidInputException e) {
            outputPresenter.prepareInvalidInputView();
        } catch (InsufficientFundsException e) {
            outputPresenter.prepareInsufficientFundsView();
        }
    }

    /**
     * Calculate the total cost of buying a stock.
     *
     * @param ticker:   The stock ticker.
     * @param quantity: The quantity of stock to buy.
     * @return The total cost of buying the stock.
     */
    private double getTotalCost(String ticker, int quantity) throws InvalidInputException {
        // TODO: we assume the ticker is only alphabetic, but in real world, it can be alphanumeric
        // TODO: handle invalid input like indentation, space, etc.
        return StockMarket.Instance().getStock(ticker).map(
                stock -> stock.getPrice() * quantity
        ).orElseThrow(InvalidInputException::new);
    }

    // custom exceptions
    static class InvalidInputException extends Exception {
    }

    static class InsufficientFundsException extends Exception {
    }
}
