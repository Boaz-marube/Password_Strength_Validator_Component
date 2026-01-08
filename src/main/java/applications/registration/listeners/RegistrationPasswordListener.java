package applications.registration.listeners;

import component.interfaces.PasswordStrengthListener;
import component.events.PasswordValidationEvent;

/**
 * Password validation listener for registration system
 */
public class RegistrationPasswordListener implements PasswordStrengthListener {

    @Override
    public void onPasswordValidated(PasswordValidationEvent event) {
        System.out.println("Password validation completed for registration");
        System.out.println("Strength: " + event.getResult().getStrengthLevel());
        System.out.println("Score: " + event.getResult().getScore() + "/6");
    }

    @Override
    public void onWeakPasswordDetected(PasswordValidationEvent event) {
        System.out.println("⚠️  WEAK PASSWORD ALERT!");
        System.out.println("Registration blocked due to weak password");
        System.out.println("Message: " + event.getResult().getMessage());
        
        String[] failedCriteria = event.getResult().getFailedCriteria();
        if (failedCriteria.length > 0) {
            System.out.println("Issues found:");
            for (String criteria : failedCriteria) {
                System.out.println("- " + criteria);
            }
        }
    }

    @Override
    public void onStrongPasswordAchieved(PasswordValidationEvent event) {
        System.out.println("✅ STRONG PASSWORD DETECTED!");
        System.out.println("Registration can proceed safely");
        System.out.println("Message: " + event.getResult().getMessage());
    }
}