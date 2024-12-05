import java.security.SecureRandom;

/**
 * Generates secure random passwords that meet specific complexity requirements.
 * 
 * References:
 * - SecureRandom: https://docs.oracle.com/javase/8/docs/api/java/security/SecureRandom.html
 * - Password Generation: https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
 * - Fisher-Yates Shuffle: https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
 */
public class PasswordGenerator {
    // Character sets for password generation
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    
    // Using SecureRandom instead of Random for better security
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Generates a password with default length of 12 characters
     * @return A secure random password
     */
    public static String generatePassword() {
        return generatePassword(12);
    }
    
    /**
     * Generates a password with specified length
     * Uses Fisher-Yates shuffle algorithm to ensure uniform distribution
     * 
     * @param length Desired password length (minimum 8)
     * @return A secure random password
     * 
     * @see https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
     */
    public static String generatePassword(int length) {
        if (length < 8) length = 8; // Minimum length of 8
        
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one of each required character type
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        
        // Fill the rest with random characters
        String allChars = LOWERCASE + UPPERCASE + NUMBERS + SPECIAL;
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password using Fisher-Yates algorithm
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
} 