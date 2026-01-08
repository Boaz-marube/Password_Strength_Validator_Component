package applications.registration.services;

import applications.registration.models.User;
import applications.registration.listeners.RegistrationPasswordListener;
import component.interfaces.PasswordValidator;
import component.interfaces.ValidationResult;
import component.impl.PasswordValidatorFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * User registration service that utilizes the Password Validation Component
 */
public class UserRegistrationService {
    private PasswordValidator passwordValidator;
    private List<User> registeredUsers;
    private RegistrationPasswordListener passwordListener;

    public UserRegistrationService() {
        // Use the password validation component
        this.passwordValidator = PasswordValidatorFactory.createForRegistration();
        this.registeredUsers = new ArrayList<>();
        this.passwordListener = new RegistrationPasswordListener();
        
        // Register event listener
        this.passwordValidator.addPasswordStrengthListener(passwordListener);
    }

    /**
     * Register a new user with password validation
     */
    public boolean registerUser(String username, String email, String password) {
        System.out.println("\n=== User Registration Process ===");
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Validating password...\n");

        // Validate password using the component
        ValidationResult result = passwordValidator.validatePassword(password);

        if (result.isValid()) {
            User newUser = new User(username, email, password);
            newUser.setActive(true);
            registeredUsers.add(newUser);
            
            System.out.println("\n✅ Registration successful!");
            System.out.println("User created: " + newUser);
            return true;
        } else {
            System.out.println("\n❌ Registration failed!");
            System.out.println("Reason: " + result.getMessage());
            
            String[] failedCriteria = result.getFailedCriteria();
            if (failedCriteria.length > 0) {
                System.out.println("Requirements not met:");
                for (String criteria : failedCriteria) {
                    System.out.println("- " + criteria);
                }
            }
            return false;
        }
    }

    /**
     * Check password strength without registration
     */
    public void checkPasswordStrength(String password) {
        System.out.println("\n=== Password Strength Check ===");
        ValidationResult result = passwordValidator.validatePassword(password);
        
        System.out.println("Password: " + password);
        System.out.println("Strength Level: " + result.getStrengthLevel());
        System.out.println("Score: " + result.getScore() + "/6");
        System.out.println("Valid: " + result.isValid());
        System.out.println("Message: " + result.getMessage());
    }

    /**
     * Get all registered users
     */
    public List<User> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers);
    }

    /**
     * Get registration statistics
     */
    public void printRegistrationStats() {
        System.out.println("\n=== Registration Statistics ===");
        System.out.println("Total registered users: " + registeredUsers.size());
        long activeUsers = registeredUsers.stream().filter(User::isActive).count();
        System.out.println("Active users: " + activeUsers);
    }
}