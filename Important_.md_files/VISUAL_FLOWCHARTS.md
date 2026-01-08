# VISUAL FLOWCHARTS FOR REPORT
## Password Strength Validation Component

---

## FLOWCHART 1: Main Password Strength Calculation Algorithm

```
                    ┌─────────────────┐
                    │      START      │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Input: Password │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Password null   │
                    │ or empty?       │
                    └─────┬───────┬───┘
                          │ YES   │ NO
                ┌─────────▼───────┐   │
                │ Return score=0  │   │
                │ VERY_WEAK       │   │
                └─────────┬───────┘   │
                          │           │
                ┌─────────▼───────┐   │
                │      END        │   │
                └─────────────────┘   │
                                      │
                            ┌─────────▼───────┐
                            │ Initialize:     │
                            │ hasUpper=false  │
                            │ hasLower=false  │
                            │ hasNumber=false │
                            │ hasSymbol=false │
                            │ score=0         │
                            └─────────┬───────┘
                                      │
                            ┌─────────▼───────┐
                            │ FOR each char   │
                            │ in password     │
                            └─────────┬───────┘
                                      │
                            ┌─────────▼───────┐
                            │ Character Type? │
                            └┬──┬──┬──┬──┬───┘
                             │  │  │  │  │
                    ┌────────▼┐ │  │  │  │
                    │Uppercase│ │  │  │  │
                    │hasUpper │ │  │  │  │
                    │= true   │ │  │  │  │
                    └────────┬┘ │  │  │  │
                             │  │  │  │  │
                    ┌────────▼──▼┐ │  │  │
                    │ Lowercase  │ │  │  │
                    │ hasLower   │ │  │  │
                    │ = true     │ │  │  │
                    └────────┬───┘ │  │  │
                             │     │  │  │
                    ┌────────▼─────▼┐ │  │
                    │    Digit      │ │  │
                    │  hasNumber    │ │  │
                    │  = true       │ │  │
                    └────────┬──────┘ │  │
                             │        │  │
                    ┌────────▼────────▼──▼┐
                    │      Symbol         │
                    │    hasSymbol        │
                    │    = true           │
                    └────────┬────────────┘
                             │
                    ┌────────▼────────┐
                    │ Calculate Score:│
                    │ IF hasUpper++   │
                    │ IF hasLower++   │
                    │ IF hasNumber++  │
                    │ IF hasSymbol++  │
                    │ IF length>=min++│
                    │ IF length>=16++ │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │  Score Value?   │
                    └┬──┬──┬──┬──┬───┘
                     │  │  │  │  │
            ┌────────▼┐ │  │  │  │
            │ >= 6    │ │  │  │  │
            │VERY_    │ │  │  │  │
            │STRONG   │ │  │  │  │
            └────────┬┘ │  │  │  │
                     │  │  │  │  │
            ┌────────▼──▼┐ │  │  │
            │ >= 5       │ │  │  │
            │ STRONG     │ │  │  │
            └────────┬───┘ │  │  │
                     │     │  │  │
            ┌────────▼─────▼┐ │  │
            │ >= 3          │ │  │
            │ MEDIUM        │ │  │
            └────────┬──────┘ │  │
                     │        │  │
            ┌────────▼────────▼──▼┐
            │ < 2               │
            │ WEAK/VERY_WEAK    │
            └────────┬──────────┘
                     │
            ┌────────▼────────┐
            │ Check Failed    │
            │ Criteria        │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ Generate        │
            │ ValidationResult│
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ Fire Events     │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ Return Result   │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │      END        │
            └─────────────────┘
```

---

## FLOWCHART 2: Event-Driven Validation Flow

```
                    ┌─────────────────┐
                    │      START      │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Password Input  │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Validate using  │
                    │ Main Algorithm  │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Create          │
                    │ ValidationEvent │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Fire General    │
                    │ Validation Event│
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Score <= 2?     │
                    └─────┬───────┬───┘
                          │ YES   │ NO
                ┌─────────▼───────┐   │
                │ Fire Weak       │   │
                │ Password Event  │   │
                └─────────┬───────┘   │
                          │           │
                          └─────┬─────┘
                                │
                    ┌─────────▼───────┐
                    │ Score >= 5?     │
                    └─────┬───────┬───┘
                          │ YES   │ NO
                ┌─────────▼───────┐   │
                │ Fire Strong     │   │
                │ Password Event  │   │
                └─────────┬───────┘   │
                          │           │
                          └─────┬─────┘
                                │
                    ┌─────────▼───────┐
                    │ Notify All      │
                    │ Listeners       │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Listeners Take  │
                    │ Actions         │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │      END        │
                    └─────────────────┘
```

---

## FLOWCHART 3: Factory Pattern Decision Tree

```
                    ┌─────────────────┐
                    │ Client Request  │
                    │ for Validator   │
                    └─────────┬───────┘
                              │
                    ┌─────────▼───────┐
                    │ Validator Type? │
                    └┬──┬──┬──┬──┬───┘
                     │  │  │  │  │
            ┌────────▼┐ │  │  │  │
            │Default  │ │  │  │  │
            └────────┬┘ │  │  │  │
                     │  │  │  │  │
            ┌────────▼──▼┐ │  │  │
            │ Banking    │ │  │  │
            │ (Strict)   │ │  │  │
            └────────┬───┘ │  │  │
                     │     │  │  │
            ┌────────▼─────▼┐ │  │
            │ Registration  │ │  │
            │ (Relaxed)     │ │  │
            └────────┬──────┘ │  │
                     │        │  │
            ┌────────▼────────▼──▼┐
            │ Custom Criteria     │
            └────────┬────────────┘
                     │
            ┌────────▼────────┐
            │ Create          │
            │ PasswordValidator│
            │ Instance        │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ Return Validator│
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │      END        │
            └─────────────────┘
```

---

## SEQUENCE DIAGRAM: Component Integration

```
User → RegistrationApp → UserService → ValidatorFactory → PasswordValidator → ValidationResult → EventListener

1. User enters password
   │
   ▼
2. RegistrationApp.registerUser()
   │
   ▼
3. UserService.registerUser()
   │
   ▼
4. ValidatorFactory.createForRegistration()
   │
   ▼
5. PasswordValidator.validatePassword()
   │
   ▼
6. ValidationResult created
   │
   ▼
7. Events fired to listeners
   │
   ▼
8. RegistrationPasswordListener.onPasswordValidated()
   │
   ▼
9. User registration completed/failed
```

---

## CLASS DIAGRAM: Component Architecture

```
┌─────────────────────────┐
│   PasswordValidator     │
│   <<interface>>         │
├─────────────────────────┤
│ + validatePassword()    │
│ + getStrengthScore()    │
│ + setValidationCriteria()│
│ + addListener()         │
│ + removeListener()      │
└─────────┬───────────────┘
          │ implements
          ▼
┌─────────────────────────┐
│ PasswordStrengthValidator│
├─────────────────────────┤
│ - criteria              │
│ - listeners             │
├─────────────────────────┤
│ + validatePassword()    │
│ + getStrengthScore()    │
│ + fireEvents()          │
└─────────┬───────────────┘
          │ uses
          ▼
┌─────────────────────────┐
│ PasswordValidatorFactory│
├─────────────────────────┤
│ + createDefault()       │
│ + createForBanking()    │
│ + createForRegistration()│
│ + createWithCriteria()  │
└─────────────────────────┘
```