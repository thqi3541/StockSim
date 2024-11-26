package interface_adapter.registration;

import use_case.registration.RegistrationInputBoundary;
import use_case.registration.RegistrationInputData;
import utility.ServiceManager;

public class RegistrationController {
    private final RegistrationInputBoundary interactor;

    public RegistrationController(RegistrationInputBoundary interactor) {
        this.interactor = interactor;

        // Register the controller with ServiceManager
        ServiceManager.Instance().registerService(RegistrationController.class, this);
    }

    public void execute(String username, String password, String confirmPassword) {
        RegistrationInputData inputData = new RegistrationInputData(username, password, confirmPassword);
        interactor.execute(inputData);
    }
}
