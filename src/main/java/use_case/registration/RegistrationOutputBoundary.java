package use_case.registration;

public interface RegistrationOutputBoundary {
    void prepareSuccessView(RegistrationOutputData outputData);
    void prepareFailView(String errorMessage);
}
