package use_case.registration;

public interface RegistrationOutputBoundary {
    void prepareSuccessView(RegistrationOutputData outputData);

    void prepareInvalidInputView(String errorMessage);

    void prepareDuplicateUsernameView(String errorMessage);

    void preparePasswordsDoNotMatchView(String errorMessage);

    void prepareWeakPasswordView(String errorMessage);

    void prepareInvalidUsernameView(String errorMessage);
}
