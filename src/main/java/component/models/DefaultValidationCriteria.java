package component.models;

import component.interfaces.ValidationCriteria;

/**
 * Default implementation of validation criteria
 */
public class DefaultValidationCriteria implements ValidationCriteria {
    private int minLength = 8;
    private int maxLength = 128;
    private boolean requiresUppercase = true;
    private boolean requiresLowercase = true;
    private boolean requiresNumbers = true;
    private boolean requiresSymbols = true;
    private int minScore = 4;

    public DefaultValidationCriteria() {}

    public DefaultValidationCriteria(int minLength, boolean requiresUppercase, 
                                   boolean requiresLowercase, boolean requiresNumbers, 
                                   boolean requiresSymbols) {
        this.minLength = minLength;
        this.requiresUppercase = requiresUppercase;
        this.requiresLowercase = requiresLowercase;
        this.requiresNumbers = requiresNumbers;
        this.requiresSymbols = requiresSymbols;
    }

    @Override
    public int getMinLength() { return minLength; }

    @Override
    public int getMaxLength() { return maxLength; }

    @Override
    public boolean requiresUppercase() { return requiresUppercase; }

    @Override
    public boolean requiresLowercase() { return requiresLowercase; }

    @Override
    public boolean requiresNumbers() { return requiresNumbers; }

    @Override
    public boolean requiresSymbols() { return requiresSymbols; }

    @Override
    public int getMinScore() { return minScore; }

    // Setters for configuration
    public void setMinLength(int minLength) { this.minLength = minLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
    public void setRequiresUppercase(boolean requiresUppercase) { this.requiresUppercase = requiresUppercase; }
    public void setRequiresLowercase(boolean requiresLowercase) { this.requiresLowercase = requiresLowercase; }
    public void setRequiresNumbers(boolean requiresNumbers) { this.requiresNumbers = requiresNumbers; }
    public void setRequiresSymbols(boolean requiresSymbols) { this.requiresSymbols = requiresSymbols; }
    public void setMinScore(int minScore) { this.minScore = minScore; }
}