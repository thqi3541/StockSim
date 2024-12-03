package utility.validations;

public class PasswordValidator {

    /**
     * Checks if the password is strong based on length and character composition.
     *
     * @param password the password to be checked
     * @return true if the password is strong, otherwise false
     */
    public static boolean isStrongPassword(String password) {
        if (password == null) {
            return false;
        }

        // Minimum length of 8 characters
        else if (password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase letters
        boolean hasUppercase = !password.equals(password.toLowerCase());

        // Check for at least one lowercase letter
        boolean hasLowercase = !password.equals(password.toUpperCase());

        // Check for at least one digit
        boolean hasDigit = password.matches(".*\\d.*");

        // Check for at least one special character
        boolean hasSpecialCharacter = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return hasUppercase && hasLowercase && hasDigit && hasSpecialCharacter;
    }
}
