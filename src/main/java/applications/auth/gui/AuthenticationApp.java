package applications.auth.gui;

import applications.auth.models.User;
import applications.auth.services.SimpleAuthService;
import component.impl.PasswordStrengthValidator;
import component.interfaces.ValidationResult;
import component.models.DefaultValidationCriteria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class AuthenticationApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SimpleAuthService authService;
    private PasswordStrengthValidator validator;
    
    // Registration components
    private JTextField regUsernameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private JProgressBar strengthBar;
    private JLabel strengthLabel;
    private JPanel criteriaPanel;
    private JTextArea suggestionArea;
    private JLabel[] criteriaLabels;
    private JLabel regUsernameError;
    private JLabel regEmailError;
    private JLabel regPasswordError;
    private JButton registerButton;
    
    // Login components
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JLabel loginError;

    public AuthenticationApp() {
        try {
            authService = new SimpleAuthService();
            validator = new PasswordStrengthValidator(new DefaultValidationCriteria());
            initComponents();
            setupLayout();
            setupListeners();
            setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initComponents() {
        setTitle("Authentication System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createRegistrationPanel();
        createLoginPanel();
        
        mainPanel.add(createRegistrationPanel(), "REGISTER");
        mainPanel.add(createLoginPanel(), "LOGIN");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "REGISTER");
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel title = new JLabel("User Registration", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JPanel usernamePanel = createFieldPanel("Username:", regUsernameField = new JTextField(20));
        regUsernameError = createErrorLabel();

        // Email field
        JPanel emailPanel = createFieldPanel("Email:", regEmailField = new JTextField(20));
        regEmailError = createErrorLabel();

        // Password field
        JPanel passwordPanel = createFieldPanel("Password:", regPasswordField = new JPasswordField(20));
        regPasswordError = createErrorLabel();

        // Password strength components
        strengthLabel = new JLabel("Enter password to check strength");
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        strengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        strengthBar = new JProgressBar(0, 100);
        strengthBar.setStringPainted(true);
        strengthBar.setPreferredSize(new Dimension(400, 25));
        strengthBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Criteria panel
        criteriaPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        criteriaPanel.setBorder(BorderFactory.createTitledBorder("Password Requirements"));
        criteriaLabels = new JLabel[5];
        String[] criteria = {
            "✓ At least 8 characters",
            "✓ Contains uppercase letter", 
            "✓ Contains lowercase letter",
            "✓ Contains number",
            "✓ Contains special character"
        };
        
        for (int i = 0; i < 5; i++) {
            criteriaLabels[i] = new JLabel(criteria[i]);
            criteriaLabels[i].setFont(new Font("Arial", Font.PLAIN, 12));
            criteriaLabels[i].setForeground(Color.GRAY);
            criteriaPanel.add(criteriaLabels[i]);
        }

        // Suggestions
        suggestionArea = new JTextArea(4, 40);
        suggestionArea.setEditable(false);
        suggestionArea.setLineWrap(true);
        suggestionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        suggestionArea.setText("Enter a password to see suggestions...");
        JPanel suggestionPanel = new JPanel(new BorderLayout());
        suggestionPanel.setBorder(BorderFactory.createTitledBorder("Suggestions"));
        suggestionPanel.add(new JScrollPane(suggestionArea), BorderLayout.CENTER);

        // Confirm password
        JPanel confirmPanel = createFieldPanel("Confirm Password:", regConfirmPasswordField = new JPasswordField(20));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("Register");
        registerButton.setEnabled(false);
        JButton clearButton = new JButton("Clear");
        JButton loginLinkButton = new JButton("Back to Login");

        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(loginLinkButton);

        // Button actions
        registerButton.addActionListener(e -> handleRegistration());
        clearButton.addActionListener(e -> clearRegistrationForm());
        loginLinkButton.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));

        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(usernamePanel);
        panel.add(regUsernameError);
        panel.add(emailPanel);
        panel.add(regEmailError);
        panel.add(passwordPanel);
        panel.add(regPasswordError);
        panel.add(Box.createVerticalStrut(10));
        panel.add(strengthLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(strengthBar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(criteriaPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(suggestionPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(confirmPanel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel usernamePanel = createFieldPanel("Username:", loginUsernameField = new JTextField(20));
        JPanel passwordPanel = createFieldPanel("Password:", loginPasswordField = new JPasswordField(20));
        
        loginError = createErrorLabel();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        JButton registerLinkButton = new JButton("Register");
        JButton forgotPasswordButton = new JButton("Forgot Password");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerLinkButton);
        buttonPanel.add(forgotPasswordButton);

        // Button actions
        loginButton.addActionListener(e -> handleLogin());
        registerLinkButton.addActionListener(e -> cardLayout.show(mainPanel, "REGISTER"));
        forgotPasswordButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Forgot password feature coming soon!"));

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(usernamePanel);
        panel.add(passwordPanel);
        panel.add(loginError);
        panel.add(Box.createVerticalStrut(20));
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel(" ");
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private void setupLayout() {
        // Layout is handled in create methods
    }

    private void setupListeners() {
        // Password validation listener
        regPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validatePassword();
                clearError(regPasswordError);
            }
        });

        // Clear error listeners
        regUsernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                clearError(regUsernameError);
            }
        });

        regEmailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                clearError(regEmailError);
            }
        });

        loginUsernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                clearError(loginError);
            }
        });

        loginPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                clearError(loginError);
            }
        });
    }

    private void validatePassword() {
        String password = new String(regPasswordField.getPassword());
        if (password.isEmpty()) {
            resetValidation();
            return;
        }

        ValidationResult result = validator.validatePassword(password);
        updateStrengthMeter(result);
        updateCriteriaChecklist(password);
        updateSuggestions(result);
        
        // Enable register button only if password is Strong or Very Strong
        String level = result.getStrengthLevel();
        registerButton.setEnabled(level.equals("STRONG") || level.equals("VERY_STRONG"));
    }

    private void updateStrengthMeter(ValidationResult result) {
        strengthBar.setValue(result.getScore());
        strengthBar.setString(result.getStrengthLevel());
        strengthLabel.setText("Password Strength: " + result.getStrengthLevel());
        
        String level = result.getStrengthLevel().toLowerCase();
        Color color;
        
        if (level.contains("very weak")) {
            color = Color.RED;
        } else if (level.contains("weak")) {
            color = new Color(255, 100, 100);
        } else if (level.contains("medium")) {
            color = Color.ORANGE;
        } else if (level.contains("strong") && level.contains("very")) {
            color = new Color(0, 150, 0);
        } else if (level.contains("strong")) {
            color = new Color(100, 255, 100);
        } else {
            color = Color.GRAY;
        }
        
        strengthBar.setForeground(color);
        strengthLabel.setForeground(color);
    }

    private void updateCriteriaChecklist(String password) {
        boolean[] checks = {
            password.length() >= 8,
            password.matches(".*[A-Z].*"),
            password.matches(".*[a-z].*"),
            password.matches(".*\\d.*"),
            password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")
        };

        for (int i = 0; i < criteriaLabels.length; i++) {
            if (checks[i]) {
                criteriaLabels[i].setForeground(Color.GREEN);
                criteriaLabels[i].setText(criteriaLabels[i].getText().replace("✓", "✅").replace("❌", "✅"));
            } else {
                criteriaLabels[i].setForeground(Color.RED);
                criteriaLabels[i].setText(criteriaLabels[i].getText().replace("✅", "❌").replace("✓", "❌"));
            }
        }
    }

    private void updateSuggestions(ValidationResult result) {
        StringBuilder suggestions = new StringBuilder();
        String[] failed = result.getFailedCriteria();
        
        if (failed.length == 0) {
            suggestions.append("✅ Excellent! Your password meets all security requirements.\n\n");
            suggestions.append("Your password is strong and secure.");
        } else {
            suggestions.append("💡 Improve your password by:\n\n");
            for (String failure : failed) {
                suggestions.append("• ").append(failure).append("\n");
            }
        }
        
        suggestionArea.setText(suggestions.toString());
    }

    private void resetValidation() {
        strengthBar.setValue(0);
        strengthBar.setString("");
        strengthLabel.setText("Enter password to check strength");
        strengthLabel.setForeground(Color.BLACK);
        suggestionArea.setText("Enter a password to see suggestions...");
        registerButton.setEnabled(false);
        
        for (JLabel label : criteriaLabels) {
            label.setForeground(Color.GRAY);
            label.setText(label.getText().replace("✅", "✓").replace("❌", "✓"));
        }
    }

    private void clearError(JLabel errorLabel) {
        errorLabel.setText(" ");
    }

    private void handleRegistration() {
        String username = regUsernameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());

        // Validate fields
        boolean hasErrors = false;

        if (username.isEmpty()) {
            regUsernameError.setText("Username is required");
            hasErrors = true;
        }

        if (email.isEmpty()) {
            regEmailError.setText("Email is required");
            hasErrors = true;
        }

        if (!password.equals(confirmPassword)) {
            regPasswordError.setText("Passwords do not match");
            hasErrors = true;
        }

        if (hasErrors) return;

        // Show loading state
        registerButton.setText("Registering...");
        registerButton.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    return authService.registerUser(username, email, password);
                } catch (Exception e) {
                    throw e;
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    registerButton.setText("Register");
                    registerButton.setEnabled(true);

                    if (success) {
                        JOptionPane.showMessageDialog(AuthenticationApp.this, 
                            "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearRegistrationForm();
                        cardLayout.show(mainPanel, "LOGIN");
                    } else {
                        regEmailError.setText("Email already exists");
                    }
                } catch (Exception e) {
                    registerButton.setText("Register");
                    registerButton.setEnabled(true);
                    JOptionPane.showMessageDialog(AuthenticationApp.this, 
                        "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginError.setText("Please enter username and password");
            return;
        }

        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                return authService.loginUser(username, password);
            }

            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        showDashboard(user);
                    } else {
                        loginError.setText("Invalid username or password");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(AuthenticationApp.this, 
                        "Login failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void clearRegistrationForm() {
        regUsernameField.setText("");
        regEmailField.setText("");
        regPasswordField.setText("");
        regConfirmPasswordField.setText("");
        clearError(regUsernameError);
        clearError(regEmailError);
        clearError(regPasswordError);
        resetValidation();
    }

    private void showDashboard(User user) {
        JFrame dashboardFrame = new JFrame("Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setSize(600, 700);
        dashboardFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername() + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(e -> {
            dashboardFrame.dispose();
            this.setVisible(true);
            loginUsernameField.setText("");
            loginPasswordField.setText("");
            clearError(loginError);
        });

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(logoutButton);

        dashboardFrame.add(panel);
        dashboardFrame.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthenticationApp());
    }
}