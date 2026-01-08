package applications.registration.models;

/**
 * User model for registration system
 */
public class User {
    private String username;
    private String email;
    private String password;
    private boolean isActive;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = false;
    }

    // Getters
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "User{username='" + username + "', email='" + email + "', active=" + isActive + "}";
    }
}