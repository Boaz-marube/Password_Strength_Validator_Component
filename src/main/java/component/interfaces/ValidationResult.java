package component.interfaces;

/**
 * Interface for password validation results
 */
public interface ValidationResult {
    boolean isValid();
    int getScore();
    String getStrengthLevel();
    String getMessage();
    String[] getFailedCriteria();
}