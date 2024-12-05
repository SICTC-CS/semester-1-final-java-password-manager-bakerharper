/**
 * Provides the user interface for the password manager.
 * Handles user input and coordinates between different components.
 * 
 * References:
 * Console Input: https://www.baeldung.com/java-scanner
 * Menu Design: https://stackoverflow.com/questions/13536679/java-create-a-menu-which-performs-various-tasks
 */

import java.util.Scanner;
import java.util.List;

public class MainMenu {
    private AccountManager accountManager;
    private FileManager fileManager;
    private Scanner scanner;
    
    public MainMenu() {
        this.accountManager = new AccountManager();
        this.fileManager = new FileManager();
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    viewAccounts();
                    break;
                case 3:
                    modifyAccount();
                    break;
                case 4:
                    deleteAccount();
                    break;
                case 5:
                    generatePassword();
                    break;
                case 0:
                    exit();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMenu() {
        System.out.println("\n=== Password Manager Menu ===");
        System.out.println("1. Add new account");
        System.out.println("2. View accounts");
        System.out.println("3. Modify account");
        System.out.println("4. Delete account");
        System.out.println("5. Generate password");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addAccount() {
        System.out.println("\n=== Add New Account ===");
        System.out.print("Enter account name: ");
        String accountName = scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Would you like to generate a password? (y/n): ");
        String password;
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            password = PasswordGenerator.generatePassword();
            System.out.println("Generated password: " + password);
        } else {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            while (!PasswordValidator.isValid(password)) {
                System.out.println("Password does not meet requirements. Please try again.");
                System.out.print("Enter password: ");
                password = scanner.nextLine();
            }
        }
        
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        
        Account account = new Account(accountName, username, password, category);
        accountManager.addAccount(account);
        fileManager.saveData(accountManager);
        System.out.println("Account added successfully!");
    }
    
    private void viewAccounts() {
        System.out.println("\n=== View Accounts ===");
        List<String> categories = accountManager.getAllCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        System.out.println("Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
        
        System.out.print("Select a category (enter number): ");
        int categoryIndex = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        if (categoryIndex < 1 || categoryIndex > categories.size()) {
            System.out.println("Invalid category selection.");
            return;
        }
        
        String selectedCategory = categories.get(categoryIndex - 1);
        List<Account> accounts = accountManager.getAccountsByCategory(selectedCategory);
        
        System.out.println("\nAccounts in " + selectedCategory + ":");
        for (Account account : accounts) {
            System.out.println("---------------------------------");
            System.out.println("Account: " + account.getAccountName());
            System.out.println("Username: " + account.getUsername());
            System.out.println("Password: " + account.getPassword());
        }
    }
    
    private void modifyAccount() {
        System.out.println("\n=== Modify Account ===");
        System.out.print("Enter account name to modify: ");
        String accountName = scanner.nextLine();
        
        // Find the account to modify
        List<Account> allAccounts = accountManager.viewAccounts();
        Account oldAccount = null;
        for (Account acc : allAccounts) {
            if (acc.getAccountName().equals(accountName)) {
                oldAccount = acc;
                break;
            }
        }
        
        if (oldAccount == null) {
            System.out.println("Account not found: " + accountName);
            return;
        }
        
        // Get new account details
        System.out.println("Enter new details (press Enter to keep current value):");
        
        System.out.print("New username [" + oldAccount.getUsername() + "]: ");
        String newUsername = scanner.nextLine();
        if (newUsername.trim().isEmpty()) {
            newUsername = oldAccount.getUsername();
        }
        
        System.out.print("Generate new password? (y/n): ");
        String newPassword = oldAccount.getPassword();
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            newPassword = PasswordGenerator.generatePassword();
            System.out.println("Generated password: " + newPassword);
        } else {
            System.out.print("New password [keep current]: ");
            String tempPass = scanner.nextLine();
            if (!tempPass.trim().isEmpty()) {
                while (!PasswordValidator.isValid(tempPass)) {
                    System.out.println("Password does not meet requirements. Please try again.");
                    System.out.print("New password: ");
                    tempPass = scanner.nextLine();
                }
                newPassword = tempPass;
            }
        }
        
        System.out.print("New category [" + oldAccount.getCategory() + "]: ");
        String newCategory = scanner.nextLine();
        if (newCategory.trim().isEmpty()) {
            newCategory = oldAccount.getCategory();
        }
        
        Account newAccount = new Account(accountName, newUsername, newPassword, newCategory);
        accountManager.modifyAccount(oldAccount, newAccount);
    }
    
    private void deleteAccount() {
        System.out.println("\n=== Delete Account ===");
        System.out.print("Enter account name to delete: ");
        String accountName = scanner.nextLine();
        
        accountManager.deleteAccount(accountName);
        fileManager.saveData(accountManager);
        System.out.println("Account deleted successfully!");
    }
    
    private void generatePassword() {
        System.out.println("\n=== Generate Password ===");
        String generatedPassword = PasswordGenerator.generatePassword();
        System.out.println("Generated password: " + generatedPassword);
    }
    
    private void exit() {
        System.out.println("Saving data...");
        fileManager.saveData(accountManager);
        System.out.println("Thank you for using Password Manager!");
        scanner.close();
    }
} 