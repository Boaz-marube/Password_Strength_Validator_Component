package component.impl;

import component.interfaces.PasswordValidator;
import component.interfaces.ValidationCriteria;
import component.models.DefaultValidationCriteria;

/**
 * Factory class for creating PasswordValidator instances
 */
public class PasswordValidatorFactory {
    
    /**
     * Creates a validator with default criteria
     */
    public static PasswordValidator createDefault() {
        return new PasswordStrengthValidator();
    }
    
    /**
     * Creates a validator with custom criteria
     */
    public static PasswordValidator createWithCriteria(ValidationCriteria criteria) {
        return new PasswordStrengthValidator(criteria);
    }
    
    /**
     * Creates a validator for banking systems (strict requirements)
     */
    public static PasswordValidator createForBanking() {
        DefaultValidationCriteria criteria = new DefaultValidationCriteria();
        criteria.setMinLength(12);
        criteria.setRequiresUppercase(true);
        criteria.setRequiresLowercase(true);
        criteria.setRequiresNumbers(true);
        criteria.setRequiresSymbols(true);
        criteria.setMinScore(5);
        return new PasswordStrengthValidator(criteria);
    }
    
    /**
     * Creates a validator for basic registration (relaxed requirements)
     */
    public static PasswordValidator createForRegistration() {
        DefaultValidationCriteria criteria = new DefaultValidationCriteria();
        criteria.setMinLength(6);
        criteria.setRequiresSymbols(false);
        criteria.setMinScore(3);
        return new PasswordStrengthValidator(criteria);
    }
}