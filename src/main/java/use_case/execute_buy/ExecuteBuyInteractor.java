package use_case.execute_buy;

import entity.*;
import utility.exceptions.ValidationException;

import java.util.Date;

/**
 * The Execute Buy Interactor.
 */
public class ExecuteBuyInteractor implements ExecuteBuyInputBoundary {

    private final ExecuteBuyDataAccessInterface dataAccess;
    private final ExecuteBuyOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ExecuteBuyInteractor class.
     * It instantiates a new Execute Buy Interactor.
     *
     * @param dataAccess     the data access
     * @param outputBoundary the output boundary
     */
    public ExecuteBuyInteractor(ExecuteBuyDataAccessInterface dataAccess, ExecuteBuyOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    /**
     * This method executes the buy transaction.
     *
     * @param data the input data
     */
    @Override
    public void execute(ExecuteBuyInputData data) {
        // TODO: commission fee?
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());

            // Get stock and quantity
            String ticker = data.ticker();

            int quantity = data.quantity();
            Stock stock = StockMarket.Instance().getStock(ticker).orElseThrow(StockNotFoundException::new);

            // Calculate some values for this transaction
            double currentPrice = stock.getPrice();
            double totalCost = currentPrice * quantity;

            if (isBalanceSufficient(currentUser, totalCost)) {
                // Deduct balance
                currentUser.deductBalance(totalCost);

                // Update portfolio
                Portfolio portfolio = currentUser.getPortfolio();
                updateOrAddStockToPortfolio(portfolio, stock, quantity, currentPrice);

                // Add transaction
                Date timestamp = new Date();
                Transaction transaction = new Transaction(timestamp, ticker, quantity, currentPrice, "buy");
                currentUser.getTransactionHistory().addTransaction(transaction);

                // Prepare success view
                outputPresenter.prepareSuccessView(new ExecuteBuyOutputData(
                        currentUser.getBalance(),
                        currentUser.getPortfolio()
                ));
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
     * This method checks if the user has sufficient balance to buy the stock.
     *
     * @param user      the user
     * @param totalCost the total cost
     * @return true if the user has sufficient balance, false otherwise
     */
    private boolean isBalanceSufficient(User user, double totalCost) {
        return user.getBalance() >= totalCost;
    }

    /**
     * This method updates the stock in the portfolio or adds a stock to the user's portfolio.
     *
     * @param portfolio    the portfolio of the user
     * @param stock        the stock the user buys
     * @param quantity     the quantity the user buys
     * @param currentPrice the current price of the stock
     */
    private void updateOrAddStockToPortfolio(Portfolio portfolio, Stock stock, int quantity, double currentPrice) {
        portfolio.getUserStock(stock.getTicker())
                .ifPresentOrElse(
                        existingStock -> existingStock.updateUserStock(currentPrice, quantity),
                        () -> portfolio.addStock(new UserStock(stock, currentPrice, quantity))
                );
    }

    static class InsufficientBalanceException extends Exception {
    }

    static class StockNotFoundException extends Exception {
    }
}