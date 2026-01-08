package applications.registration;

import applications.registration.services.UserRegistrationService;
import java.util.Scanner;

/**
 * Main Registration Application demonstrating the Password Validation Component
 */
public class RegistrationApp {
    private static UserRegistrationService registrationService;
    private static Scanner scanner;

    public static void main(String[] args) {
        registrationService = new UserRegistrationService();
        scanner = new Scanner(System.in);

        System.out.println("🔐 Welcome to User Registration System");
        System.out.println("Powered by Password Strength Validation Component\n");

        // Demo with predefined test cases
        runDemo();
        
        // Interactive mode
        runInteractiveMode();
        
        scanner.close();
    }

    private static void runDemo() {
        System.out.println("=== DEMO MODE: Testing Different Password Strengths ===\n");

        // Test cases demonstrating component functionality
        testPassword("john_doe", "john@email.com", "123");           // Very weak
        testPassword("jane_smith", "jane@email.com", "password");    // Weak  
        testPassword("bob_wilson", "bob@email.com", "Password123");  // Strong
        testPassword("alice_brown", "alice@email.com", "MyStr0ng!Pass"); // Very strong

        registrationService.printRegistrationStats();
    }

    private static void testPassword(String username, String email, String password) {
        System.out.println("Testing registration for: " + username);
        registrationService.registerUser(username, email, password);
        System.out.println("-".repeat(50));
    }

    private static void runInteractiveMode() {
        System.out.println("\n=== INTERACTIVE MODE ===");
        String choice;

        do {
            printMenu();
            choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> registerNewUser();
                case "2" -> checkPasswordOnly();
                case "3" -> registrationService.printRegistrationStats();
                case "4" -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("4"));
    }

    private static void printMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Register new user");
        System.out.println("2. Check password strength only");
        System.out.println("3. View registration statistics");
        System.out.println("4. Exit");
        System.out.print("Your choice: ");
    }

    private static void registerNewUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        registrationService.registerUser(username, email, password);
    }

    private static void checkPasswordOnly() {
        System.out.print("Enter password to check: ");
        String password = scanner.nextLine().trim();
        registrationService.checkPasswordStrength(password);
    }
}