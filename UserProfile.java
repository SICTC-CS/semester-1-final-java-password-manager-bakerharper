public class UserProfile {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String passwordHint;
    
    public UserProfile(String username, String password, String firstName, String lastName, String passwordHint) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHint = passwordHint;
    }
    
    // Getters
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getPasswordHint() {
        return passwordHint;
    }
    
    // Setters
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }
} 