package use_case.execute_sell;

import entity.Portfolio;
import entity.Stock;
import entity.Transaction;
import entity.User;
import java.rmi.ServerException;
import java.util.Date;
import utility.MarketTracker;
import utility.ServiceManager;
import utility.exceptions.ValidationException;

public class ExecuteSellInteractor implements ExecuteSellInputBoundary {
    private final ExecuteSellDataAccessInterface dataAccess;
    private final ExecuteSellOutputBoundary outputPresenter;

    public ExecuteSellInteractor(ExecuteSellDataAccessInterface dataAccess, ExecuteSellOutputBoundary outputPresenter) {
        this.dataAccess = dataAccess;
        this.outputPresenter = outputPresenter;
        ServiceManager.Instance().registerService(ExecuteSellInputBoundary.class, this);
    }

    @Override
    public void execute(ExecuteSellInputData data) {
        try {
            User currentUser = dataAccess.getUserWithCredential(data.credential());
            Stock stock = MarketTracker.Instance().getStock(data.ticker()).orElseThrow(StockNotFoundException::new);

            double currentPrice = stock.getMarketPrice();
            double totalCost = currentPrice * data.quantity();

            if (currentUser.getBalance() < totalCost) {
                throw new InsufficientMarginCallException();
            }

            currentUser.addBalance(totalCost);

            Portfolio portfolio = currentUser.getPortfolio();
            portfolio.updatePortfolio(stock, -data.quantity(), currentPrice);

            Transaction transaction = new Transaction(new Date(), data.ticker(), data.quantity(), currentPrice, "SELL");
            currentUser.getTransactionHistory().addTransaction(transaction);

            dataAccess.updateUserData(currentUser);

            outputPresenter.prepareSuccessView(new ExecuteSellOutputData(
                    currentUser.getBalance(), currentUser.getPortfolio(), currentUser.getTransactionHistory()));
        } catch (ValidationException e) {
            outputPresenter.prepareValidationExceptionView();
        } catch (StockNotFoundException e) {
            outputPresenter.prepareStockNotFoundExceptionView();
        } catch (InsufficientMarginCallException e) {
            outputPresenter.prepareInsufficientMarginCallExceptionView();
        } catch (ServerException e) {
            outputPresenter.prepareServerErrorView();
        }
    }

    static class InsufficientMarginCallException extends Exception {}

    static class StockNotFoundException extends Exception {}
}
