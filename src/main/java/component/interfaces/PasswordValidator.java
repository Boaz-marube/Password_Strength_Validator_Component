package component.interfaces;

/**
 * Main interface for password validation component
 */
public interface PasswordValidator {
    ValidationResult validatePassword(String password);
    int getStrengthScore(String password);
    void setValidationCriteria(ValidationCriteria criteria);
    void addPasswordStrengthListener(PasswordStrengthListener listener);
    void removePasswordStrengthListener(PasswordStrengthListener listener);
}