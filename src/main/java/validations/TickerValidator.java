package validations;

public class TickerValidator {
    public static boolean isTickerValid(String ticker) {
        if (ticker == null || ticker.isEmpty()) {
            return false;
        }

        boolean hasDigits = ticker.matches(".*\\d.*");
        if (hasDigits) {
            return false;
        }

        boolean isAlphaOnly = ticker.matches("^[A-Za-z]+$");
        if (!isAlphaOnly) {
            return false;
        }

        int length = ticker.length();
        return length >= 1 && length <= 8;
    }
}