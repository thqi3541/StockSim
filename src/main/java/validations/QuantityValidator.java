package validations;

public class QuantityValidator {

    public static boolean isQuantityValid(String quantity) {
        if (quantity == null || quantity.isEmpty()) {
            return false;
        }

        try {
            int value = Integer.parseInt(quantity);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}