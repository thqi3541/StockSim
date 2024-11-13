package interface_adapter.registration;

import use_case.registration.RegistrationOutputBoundary;
import use_case.registration.RegistrationOutputData;
import view.registration.RegistrationView;
import view.ViewManager;
import view.view_event.SwitchToLoginEvent;

public class RegistrationPresenter implements RegistrationOutputBoundary {
    private final RegistrationView view;

    public RegistrationPresenter(RegistrationView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(RegistrationOutputData outputData) {
        view.displayMessage(outputData.message());

        // Broadcast event to switch to login view
        ViewManager.Instance().sendViewEvent(new SwitchToLoginEvent());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        view.displayErrorMessage(errorMessage);
    }

    @Override
    public void switchToLoginView() {
        // Call the view directly to switch to login view if required
        view.switchToLoginView();
    }
}
