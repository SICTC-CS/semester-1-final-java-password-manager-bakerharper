/**
 * Represents a single account entry in the password manager.
 * Contains account details and provides methods to access and modify them.
 * 
 * References:
 * Java Beans Pattern: https://www.baeldung.com/java-beans
 * toString Override: https://stackoverflow.com/questions/10734106/how-to-override-tostring-properly-in-java
 */
public class Account {
    private String accountName;
    private String username;
    private String password;
    private String category;
    
    // Constructor
    public Account() {
    }
    
    public Account(String accountName, String username, String password, String category) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
        this.category = category;
    }
    
    // Getters
    public String getAccountName() {
        return accountName;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getCategory() {
        return category;
    }
    
    // Setters
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s",
            accountName,
            username,
            password,
            category
        );
    }
} 