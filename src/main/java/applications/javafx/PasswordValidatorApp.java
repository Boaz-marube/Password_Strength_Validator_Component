package applications.javafx;

import component.impl.PasswordStrengthValidator;
import component.interfaces.ValidationResult;
import component.models.DefaultValidationCriteria;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PasswordValidatorApp extends Application {
    private PasswordField passwordField;
    private ProgressBar strengthBar;
    private Label strengthLabel;
    private VBox criteriaBox;
    private TextArea suggestionArea;
    private PasswordStrengthValidator validator;
    private boolean isDarkTheme = false;

    @Override
    public void start(Stage primaryStage) {
        validator = new PasswordStrengthValidator(new DefaultValidationCriteria());
        
        VBox root = createMainLayout();
        Scene scene = new Scene(root, 600, 500);
        
        primaryStage.setTitle("Password Strength Validator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainLayout() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // Header
        Label title = new Label("Password Strength Validator");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        Button themeToggle = new Button("🌙 Dark Mode");
        themeToggle.setOnAction(e -> toggleTheme(root, themeToggle));
        
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(title, themeToggle);

        // Password input
        Label passwordLabel = new Label("Enter Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Type your password here...");
        passwordField.setPrefWidth(400);
        passwordField.textProperty().addListener((obs, old, text) -> validatePassword());

        // Strength meter
        strengthLabel = new Label("Password Strength: Not Evaluated");
        strengthBar = new ProgressBar(0);
        strengthBar.setPrefWidth(400);

        // Criteria checklist
        Label criteriaLabel = new Label("Password Criteria:");
        criteriaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        criteriaBox = new VBox(5);
        createCriteriaChecklist();

        // Suggestions
        Label suggestionLabel = new Label("Suggestions:");
        suggestionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        suggestionArea = new TextArea();
        suggestionArea.setPrefRowCount(4);
        suggestionArea.setEditable(false);
        suggestionArea.setWrapText(true);

        root.getChildren().addAll(header, passwordLabel, passwordField, 
                                strengthLabel, strengthBar, criteriaLabel, 
                                criteriaBox, suggestionLabel, suggestionArea);
        
        applyLightTheme(root);
        return root;
    }

    private void createCriteriaChecklist() {
        String[] criteria = {
            "✓ At least 8 characters",
            "✓ Contains uppercase letter",
            "✓ Contains lowercase letter", 
            "✓ Contains number",
            "✓ Contains special character"
        };
        
        for (String criterion : criteria) {
            Label label = new Label(criterion);
            label.setTextFill(Color.GRAY);
            criteriaBox.getChildren().add(label);
        }
    }

    private void validatePassword() {
        String password = passwordField.getText();
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
        double progress = result.getScore() / 100.0;
        strengthBar.setProgress(progress);
        strengthLabel.setText("Password Strength: " + result.getStrengthLevel() + " (" + result.getScore() + "%)");
        
        // Color coding
        if (result.getScore() < 30) {
            strengthBar.setStyle("-fx-accent: #ff4444;");
        } else if (result.getScore() < 70) {
            strengthBar.setStyle("-fx-accent: #ffaa00;");
        } else {
            strengthBar.setStyle("-fx-accent: #44ff44;");
        }
    }

    private void updateCriteriaChecklist(String password) {
        boolean[] checks = {
            password.length() >= 8,
            password.matches(".*[A-Z].*"),
            password.matches(".*[a-z].*"),
            password.matches(".*\\d.*"),
            password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")
        };

        for (int i = 0; i < criteriaBox.getChildren().size(); i++) {
            Label label = (Label) criteriaBox.getChildren().get(i);
            if (checks[i]) {
                label.setTextFill(Color.GREEN);
                label.setText(label.getText().replace("✓", "✅"));
            } else {
                label.setTextFill(Color.RED);
                label.setText(label.getText().replace("✅", "❌").replace("✓", "❌"));
            }
        }
    }

    private void updateSuggestions(ValidationResult result) {
        StringBuilder suggestions = new StringBuilder();
        String[] failed = result.getFailedCriteria();
        
        if (failed.length == 0) {
            suggestions.append("✅ Great! Your password meets all security requirements.");
        } else {
            suggestions.append("💡 Suggestions to improve your password:\n");
            for (String failure : failed) {
                suggestions.append("• ").append(failure).append("\n");
            }
        }
        
        suggestionArea.setText(suggestions.toString());
    }

    private void resetValidation() {
        strengthBar.setProgress(0);
        strengthLabel.setText("Password Strength: Not Evaluated");
        suggestionArea.setText("Enter a password to see suggestions...");
        
        for (int i = 0; i < criteriaBox.getChildren().size(); i++) {
            Label label = (Label) criteriaBox.getChildren().get(i);
            label.setTextFill(Color.GRAY);
            label.setText(label.getText().replace("✅", "✓").replace("❌", "✓"));
        }
    }

    private void toggleTheme(VBox root, Button themeToggle) {
        isDarkTheme = !isDarkTheme;
        if (isDarkTheme) {
            applyDarkTheme(root);
            themeToggle.setText("☀️ Light Mode");
        } else {
            applyLightTheme(root);
            themeToggle.setText("🌙 Dark Mode");
        }
    }

    private void applyLightTheme(VBox root) {
        root.setStyle("-fx-background-color: #f5f5f5;");
        passwordField.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
        suggestionArea.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");
    }

    private void applyDarkTheme(VBox root) {
        root.setStyle("-fx-background-color: #2b2b2b;");
        passwordField.setStyle("-fx-background-color: #404040; -fx-text-fill: white; -fx-border-color: #666;");
        suggestionArea.setStyle("-fx-background-color: #404040; -fx-text-fill: white; -fx-border-color: #666;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}