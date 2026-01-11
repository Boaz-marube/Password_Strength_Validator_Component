# Password Strength Validation Component

## Project Structure

```
src/main/java/
├── component/
│   ├── interfaces/          # Component interfaces
│   │   ├── PasswordValidator.java
│   │   ├── ValidationCriteria.java
│   │   ├── ValidationResult.java
│   │   └── PasswordStrengthListener.java
│   ├── impl/               # Component implementations
│   ├── models/             # Data models
│   │   ├── DefaultValidationCriteria.java
│   │   └── PasswordValidationResult.java
│   └── events/             # Event classes
│       └── PasswordValidationEvent.java
└── applications/           # Sample applications using the component
    ├── banking/            # Banking system example
    └── registration/       # User registration example

src/test/java/              # Unit tests
```

