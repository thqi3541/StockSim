package use_case.execute_buy;

import entity.*;

/**
 * The Execute Buy Interactor.
 */
public class ExecuteBuyInteractor implements ExecuteBuyInputBoundary {

    // custom exceptions
    class InsufficientStockException extends Exception {}
    class InsufficientFundsException extends Exception {}
    class InvalidInputException extends Exception {}

    private ExecuteBuyDataAccess dataAccess;
    private ExecuteBuyOutputBoundary outputPresenter;

    public ExecuteBuyInteractor(ExecuteBuyDataAccess dataAccess, ExecuteBuyOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputBoundary;
    }

    @Override
    public void execute(ExecuteBuyInputData data){
        User currentUser = dataAccess.getCurrentUser();
        String ticker = data.getTicker();
        int quantity = data.getQuantity();

        // TODO: handle invalid input
        // 1. check if the quantity != 0, if yes, throw ex: invalid quantity
        if (quantity <= 0) {
            // throw ex: invalid quantity

        }

        // calculate total cost from input data(Stock) -> helper getTotalCost
        try {
            // judge if cash balance can cover total cost
            if (currentUser.getBalance() >= getTotalCost(ticker, quantity)) {
                // if valid: update portfolio
                currentUser.getPortfolio().getUserStock(ticker).map(
                        stock -> {

                        }
                ).orElseThrow(() -> new InsufficientStockException());
                // if valid
                // generate new transaction

                // 1. check if quantity == 0, if yes, do nothing
                // 2. if valid, UserStock.update()
                // 3. check quantity, if 0, ask portfolio to purge

                // add to history
            } else {
                throw new InsufficientFundsException();
            }
        } catch (InvalidInputException e) {
            outputPresenter.prepareInvalidInputView();
        } catch (InsufficientFundsException e) {
            outputPresenter.prepareInsufficientFundsView();
        } catch (InsufficientStockException e) {
            outputPresenter.prepareInsufficientStocksView();
        }
    }

    /**
     * Calculate the total cost of buying a stock.
     * @param ticker: The stock ticker.
     * @param quantity: The quantity of stock to buy.
     * @return The total cost of buying the stock.
     */
    private double getTotalCost(String ticker, int quantity) throws InvalidInputException {
        // TODO: we assume the ticker is only alphabetic, but in real world, it can be alphanumeric
        // If the ticker the user passed in is in lowercase, we convert it to uppercase
        // handle invalid input like indentation, space, etc.
        return StockMarket.Instance().getStock(ticker).map(
                stock -> stock.getPrice() * quantity
        ).orElseThrow(() -> new InvalidInputException());
    }

}
