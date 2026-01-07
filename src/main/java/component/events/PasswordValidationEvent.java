package component.events;

import component.interfaces.ValidationResult;

/**
 * Event class for password validation events
 */
public class PasswordValidationEvent {
    private final String password;
    private final ValidationResult result;
    private final long timestamp;

    public PasswordValidationEvent(String password, ValidationResult result) {
        this.password = password;
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPassword() {
        return password;
    }

    public ValidationResult getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }
}