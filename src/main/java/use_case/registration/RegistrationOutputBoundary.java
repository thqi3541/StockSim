package use_case.registration;

public interface RegistrationOutputBoundary {
    void prepareSuccessView(RegistrationOutputData outputData);

    void prepareInvalidInputView(String errorMessage);

    void prepareDuplicateUsernameView(String errorMessage);
}
