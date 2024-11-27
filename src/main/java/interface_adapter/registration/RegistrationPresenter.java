package interface_adapter.registration;

import use_case.registration.RegistrationOutputBoundary;
import use_case.registration.RegistrationOutputData;
import utility.ServiceManager;
import view.ViewManager;
import view.view_events.DialogEvent;
import view.view_events.SwitchPanelEvent;

public class RegistrationPresenter implements RegistrationOutputBoundary {

    public RegistrationPresenter() {
        // Register the presenter with ServiceManager
        ServiceManager.Instance().registerService(RegistrationOutputBoundary.class, this);
    }

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

    @Override
    public void preparePasswordsDoNotMatchView(String errorMessage) {
        // Display an error message for passwords that don't match
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Error", errorMessage));
    }

    @Override
    public void prepareWeakPasswordView(String errorMessage) {
        // Display an error message for weak password
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Error", errorMessage));
    }

    @Override
    public void prepareInvalidUsernameView(String errorMessage) {
        ViewManager.Instance().broadcastEvent(new DialogEvent("Registration Error", errorMessage));
    }
}