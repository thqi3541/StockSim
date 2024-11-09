package interface_adapter.execute_buy;

import use_case.execute_buy.ExecuteBuyOutputBoundary;
import use_case.execute_buy.ExecuteBuyOutputData;

public class ExecuteBuyPresenter implements ExecuteBuyOutputBoundary {

    private final ExecuteBuyViewModel viewModel;

    public ExecuteBuyPresenter(ExecuteBuyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView() {
        viewModel.firePropertyChanged("buySuccess");
    }

    @Override
    public void prepareSuccessView(ExecuteBuyOutputData outputData) {
        viewModel.firePropertyChanged("buySuccess");
    }

    @Override
    public void prepareInsufficientFundsView() {
        viewModel.firePropertyChanged("insufficientFunds");

    }

    @Override
    public void prepareInvalidInputView() {
        viewModel.firePropertyChanged("invalidInput");
    }

    @Override
    public void prepareInsufficientStocksView() {
        viewModel.firePropertyChanged("insufficientStocks");
    }
}
