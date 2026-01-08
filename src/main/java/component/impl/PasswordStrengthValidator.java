package component.impl;

import component.interfaces.*;
import component.models.*;
import component.events.PasswordValidationEvent;
import java.util.*;

/**
 * Main implementation of the Password Strength Validation Component
 */
public class PasswordStrengthValidator implements PasswordValidator {
    private ValidationCriteria criteria;
    private List<PasswordStrengthListener> listeners;

    public PasswordStrengthValidator() {
        this.criteria = new DefaultValidationCriteria();
        this.listeners = new ArrayList<>();
    }

    public PasswordStrengthValidator(ValidationCriteria criteria) {
        this.criteria = criteria;
        this.listeners = new ArrayList<>();
    }

    @Override
    public ValidationResult validatePassword(String password) {
        if (password == null) {
            password = "";
        }

        int score = getStrengthScore(password);
        List<String> failedCriteria = getFailedCriteria(password);
        boolean isValid = failedCriteria.isEmpty() && score >= criteria.getMinScore();
        String strengthLevel = getStrengthLevel(score);
        String message = generateMessage(score, isValid);

        ValidationResult result = new PasswordValidationResult(isValid, score, strengthLevel, message, failedCriteria);
        PasswordValidationEvent event = new PasswordValidationEvent(password, result);

        // Fire events
        firePasswordValidatedEvent(event);
        if (score <= 2) {
            fireWeakPasswordEvent(event);
        } else if (score >= 5) {
            fireStrongPasswordEvent(event);
        }

        return result;
    }

    @Override
    public int getStrengthScore(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int score = 0;
        boolean hasUpper = false, hasLower = false, hasNumber = false, hasSymbol = false;

        // Check character types
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else hasSymbol = true;
        }

        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasNumber) score++;
        if (hasSymbol) score++;

        // Length scoring
        if (password.length() >= criteria.getMinLength()) score++;
        if (password.length() >= 16) score++;

        return score;
    }

    @Override
    public void setValidationCriteria(ValidationCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public void addPasswordStrengthListener(PasswordStrengthListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePasswordStrengthListener(PasswordStrengthListener listener) {
        listeners.remove(listener);
    }

    private List<String> getFailedCriteria(String password) {
        List<String> failed = new ArrayList<>();

        if (password.length() < criteria.getMinLength()) {
            failed.add("Password too short (minimum " + criteria.getMinLength() + " characters)");
        }
        if (password.length() > criteria.getMaxLength()) {
            failed.add("Password too long (maximum " + criteria.getMaxLength() + " characters)");
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasNumber = password.chars().anyMatch(Character::isDigit);
        boolean hasSymbol = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));

        if (criteria.requiresUppercase() && !hasUpper) {
            failed.add("Missing uppercase letters");
        }
        if (criteria.requiresLowercase() && !hasLower) {
            failed.add("Missing lowercase letters");
        }
        if (criteria.requiresNumbers() && !hasNumber) {
            failed.add("Missing numbers");
        }
        if (criteria.requiresSymbols() && !hasSymbol) {
            failed.add("Missing symbols");
        }

        return failed;
    }

    private String getStrengthLevel(int score) {
        if (score >= 6) return "VERY_STRONG";
        if (score >= 5) return "STRONG";
        if (score >= 3) return "MEDIUM";
        if (score >= 2) return "WEAK";
        return "VERY_WEAK";
    }

    private String generateMessage(int score, boolean isValid) {
        if (score >= 6) {
            return "Excellent password! Very strong security.";
        } else if (score >= 5) {
            return "Good password! Strong security.";
        } else if (score >= 3) {
            return "Fair password. Consider strengthening it.";
        } else if (score >= 2) {
            return "Weak password. Please improve it.";
        } else {
            return "Very weak password. Must be changed.";
        }
    }

    private void firePasswordValidatedEvent(PasswordValidationEvent event) {
        for (PasswordStrengthListener listener : listeners) {
            listener.onPasswordValidated(event);
        }
    }

    private void fireWeakPasswordEvent(PasswordValidationEvent event) {
        for (PasswordStrengthListener listener : listeners) {
            listener.onWeakPasswordDetected(event);
        }
    }

    private void fireStrongPasswordEvent(PasswordValidationEvent event) {
        for (PasswordStrengthListener listener : listeners) {
            listener.onStrongPasswordAchieved(event);
        }
    }
}