import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Utility class for encrypting and decrypting sensitive data using AES encryption.
 * Provides separate encryption for user data and password data.
 * 
 * References:
 * - AES Encryption: https://www.baeldung.com/java-aes-encryption-decryption
 * - Base64 Encoding: https://stackoverflow.com/questions/13109588/base64-encoding-in-java
 * - Cipher Usage: https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc
 */
public class EncryptionUtil {
    private static final String USER_KEY;
    private static final String PASS_KEY;
    private static final String ALGORITHM = "AES";
    
    // Load keys from configuration file
    static {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            USER_KEY = props.getProperty("USER_KEY");
            PASS_KEY = props.getProperty("PASS_KEY");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load encryption keys: " + e.getMessage());
        }
    }

    /**
     * Encrypts password data using the password-specific encryption key
     * @param value The string to encrypt
     * @return The encrypted string in Base64 format
     */
    public static String encrypt(String value) {
        return encrypt(value, PASS_KEY);
    }

    /**
     * Decrypts password data using the password-specific encryption key
     * @param encrypted The Base64 encoded encrypted string
     * @return The decrypted string
     */
    public static String decrypt(String encrypted) {
        return decrypt(encrypted, PASS_KEY);
    }

    /**
     * Encrypts user authentication data using the user-specific encryption key
     * @param value The string to encrypt
     * @return The encrypted string in Base64 format
     */
    public static String encryptUser(String value) {
        return encrypt(value, USER_KEY);
    }

    /**
     * Decrypts user authentication data using the user-specific encryption key
     * @param encrypted The Base64 encoded encrypted string
     * @return The decrypted string
     */
    public static String decryptUser(String encrypted) {
        return decrypt(encrypted, USER_KEY);
    }

    /**
     * Core encryption method using AES algorithm
     * For more details: https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc
     */
    private static String encrypt(String value, String secretKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            String encryptedString = Base64.getEncoder().encodeToString(encrypted);
            System.out.println("Debug - Encryption successful: " + value + " -> " + encryptedString);
            return encryptedString;
        } catch (Exception e) {
            System.out.println("Error encrypting: " + e.getMessage());
            e.printStackTrace();
            return value;
        }
    }

    /**
     * Core decryption method using AES algorithm
     * For more details: https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc
     */
    private static String decrypt(String encrypted, String secretKey) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            String decryptedString = new String(decrypted);
            System.out.println("Debug - Decryption successful: " + encrypted + " -> " + decryptedString);
            return decryptedString;
        } catch (Exception e) {
            System.out.println("Error decrypting: " + e.getMessage());
            e.printStackTrace();
            return encrypted;
        }
    }
} 