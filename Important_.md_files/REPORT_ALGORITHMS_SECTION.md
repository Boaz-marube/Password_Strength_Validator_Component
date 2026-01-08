# 3. ALGORITHMS USED

This section presents the core algorithms implemented in the Password Strength Validation Component, including pseudocode representations and flowchart descriptions.

## 3.1 Password Strength Calculation Algorithm

The primary algorithm evaluates password strength based on character diversity and length requirements.

### 3.1.1 Pseudocode

```
ALGORITHM: calculatePasswordStrength(password)
INPUT: password (String)
OUTPUT: ValidationResult containing score, strength level, and validation status

BEGIN
    IF password is null OR empty THEN
        RETURN score = 0, level = "VERY_WEAK"
    END IF
    
    // Initialize character type detection flags
    hasUpper = false
    hasLower = false  
    hasNumber = false
    hasSymbol = false
    score = 0
    
    // Character Analysis Phase
    FOR each character c in password DO
        IF c is uppercase letter (A-Z) THEN
            hasUpper = true
        ELSE IF c is lowercase letter (a-z) THEN
            hasLower = true
        ELSE IF c is digit (0-9) THEN
            hasNumber = true
        ELSE
            hasSymbol = true
        END IF
    END FOR
    
    // Scoring Phase
    IF hasUpper THEN score = score + 1
    IF hasLower THEN score = score + 1
    IF hasNumber THEN score = score + 1
    IF hasSymbol THEN score = score + 1
    
    // Length-based Scoring
    IF password.length >= minLength THEN score = score + 1
    IF password.length >= 16 THEN score = score + 1
    
    // Strength Classification
    IF score >= 6 THEN
        strengthLevel = "VERY_STRONG"
    ELSE IF score >= 5 THEN
        strengthLevel = "STRONG"
    ELSE IF score >= 3 THEN
        strengthLevel = "MEDIUM"
    ELSE IF score >= 2 THEN
        strengthLevel = "WEAK"
    ELSE
        strengthLevel = "VERY_WEAK"
    END IF
    
    // Validation Check
    failedCriteria = checkFailedCriteria(password)
    isValid = (failedCriteria.isEmpty() AND score >= minScore)
    
    RETURN ValidationResult(isValid, score, strengthLevel, message, failedCriteria)
END
```

### 3.1.2 Algorithm Complexity
- **Time Complexity**: O(n) where n is the password length
- **Space Complexity**: O(1) for character flags + O(k) for failed criteria list

## 3.2 Event-Driven Validation Algorithm

This algorithm implements the Observer pattern to notify listeners of validation events.

### 3.2.1 Pseudocode

```
ALGORITHM: eventDrivenValidation(password)
INPUT: password (String)
OUTPUT: Validation events fired to registered listeners

BEGIN
    // Execute main validation algorithm
    result = calculatePasswordStrength(password)
    
    // Create validation event object
    event = new PasswordValidationEvent(password, result)
    
    // Fire general validation event to all listeners
    firePasswordValidatedEvent(event)
    
    // Fire specific strength-based events
    IF result.score <= 2 THEN
        fireWeakPasswordEvent(event)
    ELSE IF result.score >= 5 THEN
        fireStrongPasswordEvent(event)
    END IF
    
    // Notify all registered listeners
    FOR each listener in listeners DO
        listener.onPasswordValidated(event)
        
        IF event represents weak password THEN
            listener.onWeakPasswordDetected(event)
        END IF
        
        IF event represents strong password THEN
            listener.onStrongPasswordAchieved(event)
        END IF
    END FOR
END
```

### 3.2.2 Event Management Operations

```
ALGORITHM: addPasswordStrengthListener(listener)
BEGIN
    listeners.add(listener)
END

ALGORITHM: removePasswordStrengthListener(listener)
BEGIN
    listeners.remove(listener)
END
```

## 3.3 Factory Pattern Algorithm

The factory algorithm creates appropriate validator instances based on system requirements.

### 3.3.1 Pseudocode

```
ALGORITHM: PasswordValidatorFactory

// Default validator creation
FUNCTION createDefault()
BEGIN
    criteria = new DefaultValidationCriteria()
    RETURN new PasswordStrengthValidator(criteria)
END

// Banking system validator (strict security requirements)
FUNCTION createForBanking()
BEGIN
    criteria = new DefaultValidationCriteria()
    criteria.setMinLength(12)
    criteria.setRequiresUppercase(true)
    criteria.setRequiresLowercase(true)
    criteria.setRequiresNumbers(true)
    criteria.setRequiresSymbols(true)
    criteria.setMinScore(5)
    RETURN new PasswordStrengthValidator(criteria)
END

// Registration system validator (relaxed requirements)
FUNCTION createForRegistration()
BEGIN
    criteria = new DefaultValidationCriteria()
    criteria.setMinLength(6)
    criteria.setRequiresSymbols(false)
    criteria.setMinScore(3)
    RETURN new PasswordStrengthValidator(criteria)
END

// Custom criteria validator
FUNCTION createWithCriteria(customCriteria)
BEGIN
    RETURN new PasswordStrengthValidator(customCriteria)
END
```

## 3.4 Component Integration Algorithm

This algorithm demonstrates how the validation component integrates with client applications.

### 3.4.1 Pseudocode

```
ALGORITHM: componentIntegration()

// User registration with password validation
FUNCTION registerUser(username, email, password)
BEGIN
    // Create appropriate validator using factory
    validator = PasswordValidatorFactory.createForRegistration()
    
    // Register event listener for validation feedback
    validator.addPasswordStrengthListener(new RegistrationPasswordListener())
    
    // Perform password validation
    result = validator.validatePassword(password)
    
    // Process validation result
    IF result.isValid THEN
        user = new User(username, email, password)
        saveUser(user)
        logSuccess("User registered successfully")
        displaySuccess("Registration completed")
    ELSE
        logFailure("Registration failed: " + result.failedCriteria)
        displayErrors(result.failedCriteria)
        requestPasswordImprovement()
    END IF
    
    // Update system statistics
    updateRegistrationStats(result.strengthLevel)
END

// Standalone password strength checking
FUNCTION checkPasswordStrength(password)
BEGIN
    validator = PasswordValidatorFactory.createDefault()
    result = validator.validatePassword(password)
    displayStrengthResult(result)
    RETURN result
END
```

## 3.5 Algorithm Flow Summary

The complete validation process follows this sequence:

1. **Input Reception**: Password string received from user interface
2. **Validator Creation**: Factory pattern creates appropriate validator instance
3. **Character Analysis**: Algorithm analyzes each character for type classification
4. **Score Calculation**: Scoring algorithm assigns points based on character diversity and length
5. **Strength Classification**: Score mapped to human-readable strength level
6. **Criteria Validation**: Password checked against system-specific requirements
7. **Event Generation**: Validation events created and fired to listeners
8. **Result Processing**: Client application processes validation result and takes appropriate action

## 3.6 Design Patterns Integration

The algorithms implement several design patterns:

- **Factory Pattern**: Creates validator instances based on system requirements
- **Observer Pattern**: Event-driven architecture for validation notifications
- **Strategy Pattern**: Different validation criteria implementations
- **Component Pattern**: Self-contained, reusable validation functionality

This algorithmic approach ensures the component is flexible, maintainable, and suitable for integration into various software systems requiring password validation functionality.