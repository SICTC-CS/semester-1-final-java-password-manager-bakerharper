import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file I/O operations for the password manager.
 * Responsible for saving account data to disk and ensuring data persistence.
 * 
 * References:
 * PrintWriter: https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html
 * File Writing: https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
 */
public class FileManager {
    private static final String FILE_PATH = "passwords.txt";
    
    public void saveData(AccountManager accountManager) {
        System.out.println("\nDebug - Saving data to file");
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            List<String> categories = accountManager.getAllCategories();
            System.out.println("Debug - Categories to save: " + categories);
            
            for (String category : categories) {
                List<Account> accounts = accountManager.getAccountsByCategory(category);
                System.out.println("Debug - Saving category '" + category + "' with " + accounts.size() + " accounts");
                
                for (Account account : accounts) {
                    String accountData = String.format("%s,%s,%s,%s",
                        account.getAccountName(),
                        account.getUsername(),
                        account.getPassword(),
                        account.getCategory()
                    );
                    System.out.println("Debug - Account data before encryption: " + accountData);
                    
                    String encryptedData = EncryptionUtil.encrypt(accountData);
                    System.out.println("Debug - Encrypted data: " + encryptedData);
                    
                    writer.println(encryptedData);
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 