# ALGORITHMS DOCUMENTATION
## Password Strength Validation Component

---

## 1. MAIN ALGORITHM: Password Strength Calculation

### Pseudocode:
```
ALGORITHM: calculatePasswordStrength(password)
INPUT: password (String)
OUTPUT: strengthScore (Integer 0-6), strengthLevel (String), validationResult (Object)

BEGIN
    IF password is null OR empty THEN
        RETURN score = 0, level = "VERY_WEAK"
    END IF
    
    // Initialize character type flags
    hasUpper = false
    hasLower = false  
    hasNumber = false
    hasSymbol = false
    score = 0
    
    // Character Analysis Loop
    FOR each character c in password DO
        IF c is uppercase letter THEN
            hasUpper = true
        ELSE IF c is lowercase letter THEN
            hasLower = true
        ELSE IF c is digit THEN
            hasNumber = true
        ELSE
            hasSymbol = true
        END IF
    END FOR
    
    // Scoring Logic
    IF hasUpper THEN score = score + 1
    IF hasLower THEN score = score + 1
    IF hasNumber THEN score = score + 1
    IF hasSymbol THEN score = score + 1
    
    // Length Scoring
    IF password.length >= minLength THEN score = score + 1
    IF password.length >= 16 THEN score = score + 1
    
    // Determine Strength Level
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
    
    // Generate validation result
    failedCriteria = checkFailedCriteria(password)
    isValid = (failedCriteria.isEmpty() AND score >= minScore)
    
    RETURN ValidationResult(isValid, score, strengthLevel, message, failedCriteria)
END
```

### Flowchart Description:
```
START
  ↓
[Input: Password String]
  ↓
{Is password null/empty?} → YES → [Return score=0, VERY_WEAK] → END
  ↓ NO
[Initialize: hasUpper=false, hasLower=false, hasNumber=false, hasSymbol=false, score=0]
  ↓
[FOR each character in password]
  ↓
{Character Type?}
  ├─ Uppercase → [hasUpper = true]
  ├─ Lowercase → [hasLower = true]  
  ├─ Digit → [hasNumber = true]
  └─ Symbol → [hasSymbol = true]
  ↓
[Calculate Score:]
[IF hasUpper: score++]
[IF hasLower: score++]
[IF hasNumber: score++]
[IF hasSymbol: score++]
[IF length >= minLength: score++]
[IF length >= 16: score++]
  ↓
{Score Value?}
  ├─ >= 6 → [VERY_STRONG]
  ├─ >= 5 → [STRONG]
  ├─ >= 3 → [MEDIUM]
  ├─ >= 2 → [WEAK]
  └─ < 2 → [VERY_WEAK]
  ↓
[Check Failed Criteria]
  ↓
[Generate Validation Result]
  ↓
[Fire Events if needed]
  ↓
[Return ValidationResult]
  ↓
END
```

---

## 2. EVENT-DRIVEN VALIDATION ALGORITHM

### Pseudocode:
```
ALGORITHM: eventDrivenValidation(password)
INPUT: password (String)
OUTPUT: ValidationEvent fired to listeners

BEGIN
    // Validate password using main algorithm
    result = calculatePasswordStrength(password)
    
    // Create validation event
    event = new PasswordValidationEvent(password, result)
    
    // Fire general validation event
    firePasswordValidatedEvent(event)
    
    // Fire specific strength events
    IF result.score <= 2 THEN
        fireWeakPasswordEvent(event)
    ELSE IF result.score >= 5 THEN
        fireStrongPasswordEvent(event)
    END IF
    
    // Notify all registered listeners
    FOR each listener in listeners DO
        listener.onPasswordValidated(event)
        
        IF event.isWeakPassword THEN
            listener.onWeakPasswordDetected(event)
        END IF
        
        IF event.isStrongPassword THEN
            listener.onStrongPasswordAchieved(event)
        END IF
    END FOR
END

ALGORITHM: addListener(listener)
BEGIN
    listeners.add(listener)
END

ALGORITHM: removeListener(listener)  
BEGIN
    listeners.remove(listener)
END
```

### Event Flow Sequence:
```
User Input → PasswordValidator → ValidationResult → Event Creation → Event Firing → Listeners Notified → Actions Taken
```

---

## 3. FACTORY PATTERN ALGORITHM

### Pseudocode:
```
ALGORITHM: PasswordValidatorFactory

// Default validator creation
FUNCTION createDefault()
BEGIN
    criteria = new DefaultValidationCriteria()
    RETURN new PasswordStrengthValidator(criteria)
END

// Custom criteria validator
FUNCTION createWithCriteria(customCriteria)
BEGIN
    RETURN new PasswordStrengthValidator(customCriteria)
END

// Banking system validator (strict)
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

// Registration system validator (relaxed)
FUNCTION createForRegistration()
BEGIN
    criteria = new DefaultValidationCriteria()
    criteria.setMinLength(6)
    criteria.setRequiresSymbols(false)
    criteria.setMinScore(3)
    RETURN new PasswordStrengthValidator(criteria)
END
```

### Factory Decision Tree:
```
Client Request
    ↓
{Validator Type?}
    ├─ Default → [Create with DefaultCriteria]
    ├─ Banking → [Create with StrictCriteria (12+ chars, all types, score≥5)]
    ├─ Registration → [Create with RelaxedCriteria (6+ chars, no symbols required)]
    └─ Custom → [Create with ProvidedCriteria]
    ↓
[Return PasswordValidator Instance]
```

---

## 4. COMPONENT INTEGRATION ALGORITHM

### Pseudocode:
```
ALGORITHM: componentIntegration()

// Registration Service Integration
FUNCTION registerUser(username, email, password)
BEGIN
    // Create validator using factory
    validator = PasswordValidatorFactory.createForRegistration()
    
    // Add event listener
    validator.addPasswordStrengthListener(new RegistrationPasswordListener())
    
    // Validate password
    result = validator.validatePassword(password)
    
    IF result.isValid THEN
        user = new User(username, email, password)
        saveUser(user)
        logSuccess("User registered successfully")
    ELSE
        logFailure("Registration failed: " + result.failedCriteria)
        displayErrors(result.failedCriteria)
    END IF
    
    // Update statistics
    updateRegistrationStats(result.strengthLevel)
END

// Standalone validation
FUNCTION checkPasswordStrength(password)
BEGIN
    validator = PasswordValidatorFactory.createDefault()
    result = validator.validatePassword(password)
    displayResult(result)
    RETURN result
END
```

### Component Architecture Flow:
```
Application Layer (RegistrationApp)
    ↓
Service Layer (UserRegistrationService)
    ↓
Component Layer (PasswordValidator via Factory)
    ↓
Event Layer (PasswordValidationEvent)
    ↓
Listener Layer (RegistrationPasswordListener)
    ↓
Model Layer (User, ValidationResult)
```

---

## ALGORITHM COMPLEXITY ANALYSIS

### Time Complexity:
- **Password Strength Calculation**: O(n) where n = password length
- **Event Firing**: O(m) where m = number of listeners
- **Factory Creation**: O(1)
- **Overall Validation**: O(n + m)

### Space Complexity:
- **Character Analysis**: O(1) - fixed boolean flags
- **Failed Criteria List**: O(k) where k = number of failed criteria
- **Event Storage**: O(1) per event
- **Overall**: O(k)

---

## DESIGN PATTERNS USED

1. **Factory Pattern**: PasswordValidatorFactory for creating validators
2. **Observer Pattern**: Event listeners for validation results
3. **Strategy Pattern**: Different validation criteria implementations
4. **Component Pattern**: Self-contained, reusable validation component