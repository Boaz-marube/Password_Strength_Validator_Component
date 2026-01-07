package component.models;

import component.interfaces.ValidationResult;
import java.util.List;

/**
 * Implementation of validation result
 */
public class PasswordValidationResult implements ValidationResult {
    private final boolean valid;
    private final int score;
    private final String strengthLevel;
    private final String message;
    private final String[] failedCriteria;

    public PasswordValidationResult(boolean valid, int score, String strengthLevel, 
                                  String message, List<String> failedCriteria) {
        this.valid = valid;
        this.score = score;
        this.strengthLevel = strengthLevel;
        this.message = message;
        this.failedCriteria = failedCriteria.toArray(new String[0]);
    }

    @Override
    public boolean isValid() { return valid; }

    @Override
    public int getScore() { return score; }

    @Override
    public String getStrengthLevel() { return strengthLevel; }

    @Override
    public String getMessage() { return message; }

    @Override
    public String[] getFailedCriteria() { return failedCriteria; }
}