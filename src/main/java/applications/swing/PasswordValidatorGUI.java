package applications.swing;

import component.impl.PasswordStrengthValidator;
import component.interfaces.ValidationResult;
import component.models.DefaultValidationCriteria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PasswordValidatorGUI extends JFrame {
    private JPasswordField passwordField;
    private JLabel strengthLabel;
    private JProgressBar strengthBar;
    private JPanel criteriaPanel;
    private JTextArea suggestionArea;
    private PasswordStrengthValidator validator;
    private JLabel[] criteriaLabels;

    public PasswordValidatorGUI() {
        validator = new PasswordStrengthValidator(new DefaultValidationCriteria());
        initComponents();
        setupLayout();
        setupListeners();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Password Strength Validator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        strengthLabel = new JLabel("Enter password to check strength");
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        strengthBar = new JProgressBar(0, 100);
        strengthBar.setStringPainted(true);
        strengthBar.setPreferredSize(new Dimension(400, 25));
        
        criteriaPanel = new JPanel(new GridLayout(5, 1, 5, 5));
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
        
        suggestionArea = new JTextArea(6, 40);
        suggestionArea.setEditable(false);
        suggestionArea.setLineWrap(true);
        suggestionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        suggestionArea.setText("Enter a password to see suggestions...");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = new JLabel("Password Strength Validator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Password input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Password: "));
        inputPanel.add(passwordField);
        
        // Strength display
        JPanel strengthPanel = new JPanel();
        strengthPanel.setLayout(new BoxLayout(strengthPanel, BoxLayout.Y_AXIS));
        strengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        strengthBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        strengthPanel.add(strengthLabel);
        strengthPanel.add(Box.createVerticalStrut(10));
        strengthPanel.add(strengthBar);
        
        // Criteria
        JPanel criteriaContainer = new JPanel(new BorderLayout());
        criteriaContainer.setBorder(BorderFactory.createTitledBorder("Password Requirements"));
        criteriaContainer.add(criteriaPanel, BorderLayout.CENTER);
        
        // Suggestions
        JPanel suggestionContainer = new JPanel(new BorderLayout());
        suggestionContainer.setBorder(BorderFactory.createTitledBorder("Suggestions"));
        suggestionContainer.add(new JScrollPane(suggestionArea), BorderLayout.CENTER);
        
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(strengthPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(criteriaContainer);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(suggestionContainer);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validatePassword();
            }
        });
    }

    private void validatePassword() {
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            resetValidation();
            return;
        }

        ValidationResult result = validator.validatePassword(password);
        updateStrengthMeter(result);
        updateCriteriaChecklist(password);
        updateSuggestions(result);
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
            color = new Color(255, 100, 100); // Light red
        } else if (level.contains("medium")) {
            color = Color.ORANGE;
        } else if (level.contains("strong") && level.contains("very")) {
            color = new Color(0, 150, 0); // Dark green
        } else if (level.contains("strong")) {
            color = new Color(100, 255, 100); // Light green
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
        
        for (JLabel label : criteriaLabels) {
            label.setForeground(Color.GRAY);
            label.setText(label.getText().replace("✅", "✓").replace("❌", "✓"));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasswordValidatorGUI());
    }
}