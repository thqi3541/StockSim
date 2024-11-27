package validations;

public class PasswordValidator {

    // Configuration flags to enable/disable checks
    private static final boolean REQUIRE_UPPERCASE = false;
    private static final boolean REQUIRE_LOWERCASE = false;
    private static final boolean REQUIRE_DIGIT = true;
    private static final boolean REQUIRE_SPECIAL_CHARACTER = false;
    private static final int MIN_LENGTH = 6;

    // Regular expressions for checks
    private static final String DIGIT_REGEX = ".*\\d.*";
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";
    private static final String LOWERCASE_REGEX = ".*[a-z].*";
    private static final String SPECIAL_CHAR_REGEX = ".*[!@#$%^&*(),.?\":{}|<>].*";

    /**
     * Checks if the password is strong based on configurable complexity rules.
     *
     * @param password the password to be checked
     * @return true if the password meets the configured complexity requirements, otherwise false
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }

        if (REQUIRE_UPPERCASE && !password.matches(UPPERCASE_REGEX)) {
            return false;
        }

        if (REQUIRE_LOWERCASE && !password.matches(LOWERCASE_REGEX)) {
            return false;
        }

        if (REQUIRE_DIGIT && !password.matches(DIGIT_REGEX)) {
            return false;
        }

        return !REQUIRE_SPECIAL_CHARACTER || password.matches(SPECIAL_CHAR_REGEX);
    }
}
