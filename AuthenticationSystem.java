/**
 * Handles user authentication and registration for the password manager.
 * Manages user profiles and provides login/registration functionality.
 * 
 * References:
 * User Authentication: https://www.baeldung.com/java-authentication-frameworks
 * File-based User Storage: https://stackoverflow.com/questions/19285636/user-authentication-storing-password
 */

import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationSystem {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final String USERS_FILE = "users.txt";
    private UserProfile currentUser;
    private Map<String, UserProfile> users;
    
    public AuthenticationSystem() {
        users = new HashMap<>();
        loadUsers();
    }
    
    public boolean authenticate() {
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;
        
        while (attempts < MAX_LOGIN_ATTEMPTS) {
            System.out.println("1. Login\n2. Register new user");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            if (choice == 1) {
                if (login()) {
                    return true;
                }
                attempts++;
                System.out.println("Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - attempts));
            } else if (choice == 2) {
                register();
                // After registration, user still needs to login
                continue;
            }
        }
        System.out.println("Maximum login attempts exceeded. Program terminating.");
        return false;
    }
    
    private boolean login() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        UserProfile user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful! Welcome " + user.getFirstName() + "!");
            return true;
        }
        
        System.out.println("Invalid username or password.");
        return false;
    }
    
    private void register() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== User Registration ===");
        
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (!users.containsKey(username)) {
                break;
            }
            System.out.println("Username already exists. Please choose another.");
        }
        
        String password;
        do {
            System.out.println("Password requirements:");
            System.out.println("- At least 8 characters");
            System.out.println("- At least 1 capital letter");
            System.out.println("- At least 1 number");
            System.out.println("- At least 1 special character");
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            
            if (!PasswordValidator.isValid(password)) {
                System.out.println("Password does not meet requirements. Please try again.");
            }
        } while (!PasswordValidator.isValid(password));
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter password hint: ");
        String passwordHint = scanner.nextLine();
        
        UserProfile newUser = new UserProfile(username, password, firstName, lastName, passwordHint);
        users.put(username, newUser);
        saveUsers();
        
        System.out.println("Registration successful! Please login with your credentials.");
    }
    
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    // Decrypt the line
                    String decryptedLine = EncryptionUtil.decryptUser(line);
                    
                    // Split the decrypted data
                    String[] parts = decryptedLine.split(",");
                    if (parts.length == 5) {
                        UserProfile user = new UserProfile(
                            parts[0], // username
                            parts[1], // password
                            parts[2], // firstName
                            parts[3], // lastName
                            parts[4]  // passwordHint
                        );
                        users.put(parts[0], user);
                    }
                } catch (Exception e) {
                    System.out.println("Error decrypting user data: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, that's okay
        }
    }
    
    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (UserProfile user : users.values()) {
                // Create a string with user data
                String userData = String.format("%s,%s,%s,%s,%s",
                    user.getUsername(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPasswordHint()
                );
                
                // Encrypt the data
                String encryptedData = EncryptionUtil.encryptUser(userData);
                
                // Write encrypted data to file
                writer.println(encryptedData);
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
} 