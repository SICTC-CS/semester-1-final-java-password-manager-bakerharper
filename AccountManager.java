import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;
import java.util.Base64;

/*
 * References:
 * HashMap usage: https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
 * File operations: https://www.baeldung.com/java-write-to-file
 * Map computeIfAbsent: https://stackoverflow.com/questions/21714275/java-8-mapcomputeifabsent-method
 */
public class AccountManager {
    private Map<String, List<Account>> accountsByCategory;
    private static final String FILE_PATH = "passwords.txt";
    private FileManager fileManager;
    
    public AccountManager() {
        this.accountsByCategory = new HashMap<>();
        this.fileManager = new FileManager();
        loadAccountsFromFile();
    }
    
    public List<Account> viewAccounts() {
        System.out.println("Debug - Viewing all accounts");
        List<Account> allAccounts = new ArrayList<>();
        for (List<Account> accounts : accountsByCategory.values()) {
            allAccounts.addAll(accounts);
        }
        System.out.println("Debug - Total accounts found: " + allAccounts.size());
        System.out.println("Debug - Categories: " + accountsByCategory.keySet());
        return allAccounts;
    }
    
    private void loadAccountsFromFile() {
        accountsByCategory.clear();
        File file = new File(FILE_PATH);
        
        System.out.println("\nDebug - Loading accounts from: " + FILE_PATH);
        System.out.println("Debug - File exists: " + file.exists());
        
        if (!file.exists()) {
            System.out.println("No password file found. Creating new file.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return;
        }
        
        try {
            // First, let's see what's in the file
            System.out.println("\nDebug - File contents:");
            BufferedReader debugReader = new BufferedReader(new FileReader(file));
            String debugLine;
            while ((debugLine = debugReader.readLine()) != null) {
                System.out.println("Raw line: " + debugLine);
            }
            debugReader.close();
        } catch (IOException e) {
            System.out.println("Error reading file for debug: " + e.getMessage());
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        System.out.println("\nDebug - Processing line: " + line);
                        String decryptedLine = EncryptionUtil.decrypt(line);
                        System.out.println("Debug - Decrypted line: " + decryptedLine);
                        
                        String[] parts = decryptedLine.split(",");
                        System.out.println("Debug - Parts length: " + parts.length);
                        if (parts.length >= 4) {
                            Account account = new Account(
                                parts[0],  // accountName
                                parts[1],  // username
                                parts[2],  // password
                                parts[3]   // category
                            );
                            String category = account.getCategory();
                            accountsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(account);
                            System.out.println("Debug - Added account: " + account.getAccountName() + " to category: " + category);
                        }
                    } catch (Exception e) {
                        System.out.println("Debug - Error processing line: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\nDebug - Loaded categories: " + accountsByCategory.keySet());
        for (Map.Entry<String, List<Account>> entry : accountsByCategory.entrySet()) {
            System.out.println("Debug - Category '" + entry.getKey() + "' has " + entry.getValue().size() + " accounts");
            for (Account acc : entry.getValue()) {
                System.out.println("  - " + acc.getAccountName());
            }
        }
    }

    public void addAccount(Account account) {
        System.out.println("\nDebug - Adding account: " + account.getAccountName());
        String category = account.getCategory();
        accountsByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(account);
        System.out.println("Debug - Added to category: " + category);
        System.out.println("Debug - Current categories: " + accountsByCategory.keySet());
        
        // Save directly to file first
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            String accountData = String.format("%s,%s,%s,%s",
                account.getAccountName(),
                account.getUsername(),
                account.getPassword(),
                account.getCategory()
            );
            String encryptedData = EncryptionUtil.encrypt(accountData);
            writer.println(encryptedData);
            System.out.println("Debug - Written to file: " + encryptedData);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        
        // Then update in-memory data
        fileManager.saveData(this);
        System.out.println("Added account: " + account.getAccountName());
        
        // Verify the file was written
        System.out.println("\nDebug - Verifying file contents after add:");
        loadAccountsFromFile();
    }

    public void modifyAccount(Account oldAccount, Account newAccount) {
        String oldCategory = oldAccount.getCategory();
        List<Account> categoryAccounts = accountsByCategory.get(oldCategory);
        
        if (categoryAccounts != null) {
            for (int i = 0; i < categoryAccounts.size(); i++) {
                if (categoryAccounts.get(i).getAccountName().equals(oldAccount.getAccountName())) {
                    categoryAccounts.set(i, newAccount);
                    fileManager.saveData(this);
                    System.out.println("Modified: " + oldAccount.getAccountName());
                    return;
                }
            }
        }
        System.out.println("Account not found: " + oldAccount.getAccountName());
    }

    public void deleteAccount(String accountName) {
        boolean found = false;
        
        for (List<Account> accounts : accountsByCategory.values()) {
            for (Iterator<Account> iterator = accounts.iterator(); iterator.hasNext();) {
                Account account = iterator.next();
                if (account.getAccountName().equals(accountName)) {
                    iterator.remove();
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        
        if (found) {
            fileManager.saveData(this);
            System.out.println("Deleted: " + accountName);
        } else {
            System.out.println("Account not found: " + accountName);
        }
    }

    public List<String> getAllCategories() {
        return new ArrayList<>(accountsByCategory.keySet());
    }
    
    public List<Account> getAccountsByCategory(String category) {
        return accountsByCategory.getOrDefault(category, new ArrayList<>());
    }
} 