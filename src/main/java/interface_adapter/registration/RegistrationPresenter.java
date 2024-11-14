package interface_adapter.registration;

import use_case.registration.RegistrationOutputBoundary;
import use_case.registration.RegistrationOutputData;
import view.view_events.DialogEvent;
import view.view_events.SwitchPanelEvent;
import utility.ViewManager;

public class RegistrationPresenter implements RegistrationOutputBoundary {

    @Override
    public void prepareSuccessView(RegistrationOutputData outputData) {
        // Display a success message to the user
        String successMessage = outputData.message();
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Successful", successMessage));

        // After displaying the success message, switch to the login view
        switchToLoginView();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Display an error message to the user
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Error", errorMessage));
    }

    @Override
    public void switchToLoginView() {
        // Switch to the login view explicitly
        ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel"));
    }
}