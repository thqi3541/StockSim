package utility.validations;

public class UsernameValidator {

  public static String validateUsername(String username) {
    if (username.length() < 5) {
      return "Username must be at least 5 characters long.";
    } else if (username.length() > 20) {
      return "Username must be between 5 and 20 characters.";
    } else if (!username.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
      return "Username must start with a letter and contain only letters, digits, or underscores.";
    } else if (username.contains("admin") || username.contains("root")) {
      return "Username cannot contain restricted words such as 'admin' or 'root'.";
    }
    return "";
  }
}
