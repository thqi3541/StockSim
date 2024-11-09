package use_case.execute_buy;

/**
 * The Execute Buy Interactor.
 */
public class ExecuteBuyInteractor implements ExecuteBuyInputBoundary {

    private ExecuteBuyDataAccess dataAccess;

    public ExecuteBuyInteractor(ExecuteBuyDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(ExecuteBuyInputData data){
        User currentUser = dataAccess.getCurrentUser();
        // calculate total cost from input data(Stock) -> helper getTotalCost
        try {
            getTotalCost(data.getTicker(), data.getQuantity())
        } catch (Exception e) {
            prepareFailView()
        }

        // compare with user balance

        // if valid: update portfolio

        // generate new transaction

        // add to history

        // else: do noting, return fail message
    }

    /**
     * Calculate the total cost of buying a stock.
     * @param ticker: The stock ticker.
     * @param quantity: The quantity of stock to buy.
     * @return The total cost of buying the stock.
     */
    private double getTotalCost(String ticker, int quantity) throws Exception {
        // TODO: we assume the ticker is only alphabetic, but in real world, it can be alphanumeric
        // If the ticker the user passed in is in lowercase, we convert it to uppercase
        // handle invalid input like indentation, space, etc.
        return StockMarket.Instance().getStock(ticker).map(
                stock -> stock.getPrice() * quantity
        ).orElseThrow(() -> new Exception("Cannot find ticker in stock market."));
    }

    private void prepareFailView() {

    }

}
