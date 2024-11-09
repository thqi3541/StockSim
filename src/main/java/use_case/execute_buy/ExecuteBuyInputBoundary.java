package use_case.execute_buy;

/**
 * The Execute Buy Use Case.
 */
public interface ExecuteBuyInputBoundary {

    /**
     * Execute the Buy Use Case.
     * @param input the intput data for this use case
     */
    void execute(ExecuteBuyInputData input);

}
