package use_case.execute_sell;

import entity.*;
import utility.MarketTracker;
import utility.ServiceManager;
import utility.exceptions.ValidationException;

import java.rmi.ServerException;
import java.util.Date;

/**
 * The interactor for the Sell Stock use case
 */
public class ExecuteSellInteractor implements ExecuteSellInputBoundary {

    private final ExecuteSellDataAccessInterface dataAccess;
    private final ExecuteSellOutputBoundary outputPresenter;

    /**
     * This is the constructor of the ExecuteSellInteractor class.
     * It instantiates a new Execute Sell Interactor.
     *
     * @param dataAccess     the data access
     * @param outputBoundary the output boundary
     */
    public ExecuteSellInteractor(ExecuteSellDataAccessInterface dataAccess, ExecuteSellOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
        ServiceManager.Instance().registerService(ExecuteSellInputBoundary.class, this);
    }

    /**
     * This method executes the sell transaction.
     *
     * @param data the input data
     */
    @Override
    public void execute(ExecuteSellInputData data) {
        try {
            // Get current user
            User currentUser = dataAccess.getUserWithCredential(data.credential());

            // Get stock and quantity the user wants to sell
            String ticker = data.ticker();
            int quantity = data.quantity();
            Stock stock = MarketTracker.Instance().getStock(ticker).orElseThrow(ExecuteSellInteractor.StockNotFoundException::new);

            // Calculate some values for this transaction
            // totalProfit is the money you get from selling the stock
            // totalMarginCall: to make sure you have enough money to sell them back
            double currentPrice = stock.getMarketPrice();
            double totalProfit = currentPrice * quantity;
            double totalAssets = currentUser.getTotalAssetsValue();

            // check if the input quantity is invalid
            if (quantity <= 0) {
                throw new InvalidQuantityException();
            }

            if (totalProfit <= totalAssets) {
                // Add balance
                currentUser.addBalance(totalProfit);

                // Update portfolio by decreasing the quantity of the stock
                Portfolio portfolio = currentUser.getPortfolio();
                portfolio.updatePortfolio(stock, -quantity, currentPrice);

                // Add transaction
                Transaction transaction = new Transaction(new Date(), ticker, quantity, currentPrice, "SELL");
                currentUser.getTransactionHistory().addTransaction(transaction);

                // update user data
                dataAccess.updateUserData(currentUser);

                // Prepare success view
                outputPresenter.prepareSuccessView(new ExecuteSellOutputData(
                        currentUser.getBalance(),
                        currentUser.getPortfolio(),
                        currentUser.getTransactionHistory()
                ));
            } else {
                throw new ExecuteSellInteractor.InsufficientMarginCallException();
            }
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        } catch (ServerException e) {
            outputPresenter.prepareServerErrorView();
        } catch (ExecuteSellInteractor.StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundExceptionView();
        } catch (ExecuteSellInteractor.InsufficientMarginCallException e) {
            outputPresenter.prepareInsufficientMarginCallExceptionView();
        } catch (InvalidQuantityException e) {
            outputPresenter.prepareInvalidQuantityExceptionView("Quantity must be greater than 0");
        }
    }


    static class InsufficientMarginCallException extends Exception {
    }

    static class StockNotFoundException extends Exception {
    }

    static class InvalidQuantityException extends Exception {
    }
}
