package component.interfaces;

import component.events.PasswordValidationEvent;

/**
 * Event listener interface for password validation events
 */
public interface PasswordStrengthListener {
    void onPasswordValidated(PasswordValidationEvent event);
    void onWeakPasswordDetected(PasswordValidationEvent event);
    void onStrongPasswordAchieved(PasswordValidationEvent event);
}