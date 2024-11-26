package use_case.execute_buy;

import entity.*;
import utility.MarketTracker;
import utility.ServiceManager;
import utility.exceptions.ValidationException;

import java.util.Date;

/**
 * The interactor for the Buy Stock use case
 */
public class ExecuteBuyInteractor implements ExecuteBuyInputBoundary {

    private final ExecuteBuyDataAccessInterface dataAccess;
    private final ExecuteBuyOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ExecuteBuyInteractor class. It instantiates a new Execute Buy
     * Interactor.
     *
     * @param dataAccess     the data access
     * @param outputBoundary the output boundary
     */
    public ExecuteBuyInteractor(
            ExecuteBuyDataAccessInterface dataAccess, ExecuteBuyOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
        ServiceManager.Instance().registerService(ExecuteBuyInputBoundary.class, this);
    }

    /**
     * This method executes the buy transaction.
     *
     * @param data the input data
     */
    @Override
    public void execute(ExecuteBuyInputData data) {
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());

            // Get stock and quantity
            String ticker = data.ticker();
            int quantity = data.quantity();
            Stock stock =
                    MarketTracker.Instance().getStock(ticker).orElseThrow(StockNotFoundException::new);

            // Calculate some values for this transaction
            double currentPrice = stock.getMarketPrice();
            double totalCost = currentPrice * quantity;

            if (currentUser.getBalance() >= totalCost) {
                // Deduct balance
                currentUser.deductBalance(totalCost);

                // Update portfolio
                Portfolio portfolio = currentUser.getPortfolio();
                updateOrAddStockToPortfolio(portfolio, stock, quantity, currentPrice);

                // Add transaction
                Date timestamp = new Date();
                Transaction transaction = new Transaction(timestamp, ticker, quantity, currentPrice, "BUY");
                currentUser.getTransactionHistory().addTransaction(transaction);

                // Prepare success view
                outputPresenter.prepareSuccessView(
                        new ExecuteBuyOutputData(
                                currentUser.getBalance(),
                                currentUser.getPortfolio(),
                                currentUser.getTransactionHistory()));
            } else {
                throw new InsufficientBalanceException();
            }
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        } catch (StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundExceptionView();
        } catch (InsufficientBalanceException e) {
            outputPresenter.prepareInsufficientBalanceExceptionView();
        }
    }

    /**
     * This method updates the stock in the portfolio or adds a stock to the user's portfolio.
     *
     * @param portfolio    the portfolio of the user
     * @param stock        the stock the user buys
     * @param quantity     the quantity the user buys
     * @param currentPrice the current executionPrice of the stock
     */
    private void updateOrAddStockToPortfolio(
            Portfolio portfolio, Stock stock, int quantity, double currentPrice) {
        portfolio
                .getUserStock(stock.getTicker())
                .ifPresentOrElse(
                        existingUserStock -> existingUserStock.updateUserStock(currentPrice, quantity),
                        () -> portfolio.addUserStock(new UserStock(stock, currentPrice, quantity)));
    }

    static class InsufficientBalanceException extends Exception {
    }

    static class StockNotFoundException extends Exception {
    }
}
