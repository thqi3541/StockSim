package interface_adapter.registration;

import use_case.registration.RegistrationInputBoundary;
import use_case.registration.RegistrationInputData;

public class RegistrationController {
    private final RegistrationInputBoundary interactor;

    public RegistrationController(RegistrationInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void register(String userId, String password, String confirmPassword) {
        RegistrationInputData inputData = new RegistrationInputData(userId, password, confirmPassword);
        interactor.execute(inputData);
    }
}
