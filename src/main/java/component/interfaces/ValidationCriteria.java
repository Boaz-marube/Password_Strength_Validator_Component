package component.interfaces;

/**
 * Interface for configuring password validation criteria
 */
public interface ValidationCriteria {
    int getMinLength();
    int getMaxLength();
    boolean requiresUppercase();
    boolean requiresLowercase();
    boolean requiresNumbers();
    boolean requiresSymbols();
    int getMinScore();
}