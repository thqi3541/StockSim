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
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Successful", outputData.message()));
        ViewManager.Instance().broadcastEvent(new SwitchPanelEvent("LogInPanel"));
    }

    @Override
    public void prepareInvalidInputView(String errorMessage) {
        // Display an error message to the user
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Error", errorMessage));
    }

    @Override
    public void prepareDuplicateUsernameView(String errorMessage) {
        // Display an error message for duplicate usernames
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Error", errorMessage));
    }
}