package interface_adapter.execute_buy;

import interface_adapter.ViewModel;

/**
 * The View Model for the Buy View.
 */
public class ExecuteBuyViewModel extends ViewModel<ExecuteBuyState> {

    public ExecuteBuyViewModel() {
        super("buy");
        setState(new ExecuteBuyState());
    }
}
