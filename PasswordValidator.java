public class PasswordValidator {
    public static boolean isValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (specialChars.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        
        return hasCapital && hasNumber && hasSpecial;
    }
} 