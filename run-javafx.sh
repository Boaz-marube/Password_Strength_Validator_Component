#!/bin/bash

# JavaFX Password Validator Launcher
echo " Starting Password Strength Validator GUI..."

# Check if JavaFX is available
if ! java --list-modules | grep -q javafx; then
    echo " JavaFX not found. Please install OpenJFX or use a JDK with JavaFX included."
    echo " Try: sudo apt install openjfx (Ubuntu/Debian) or brew install openjfx (macOS)"
    exit 1
fi

# Compile and run
cd "$(dirname "$0")"
javac --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -d . src/main/java/applications/javafx/*.java src/main/java/component/interfaces/*.java src/main/java/component/impl/*.java src/main/java/component/models/*.java src/main/java/component/events/*.java

if [ $? -eq 0 ]; then
    java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml applications.javafx.PasswordValidatorApp
else
    echo " Compilation failed. Trying alternative JavaFX path..."
    # Try alternative paths for different systems
    for path in "/usr/lib/jvm/javafx-sdk/lib" "/opt/javafx/lib" "$HOME/javafx/lib"; do
        if [ -d "$path" ]; then
            echo " Trying JavaFX path: $path"
            javac --module-path "$path" --add-modules javafx.controls,javafx.fxml -d . src/main/java/applications/javafx/*.java src/main/java/component/interfaces/*.java src/main/java/component/impl/*.java src/main/java/component/models/*.java src/main/java/component/events/*.java
            if [ $? -eq 0 ]; then
                java --module-path "$path" --add-modules javafx.controls,javafx.fxml applications.javafx.PasswordValidatorApp
                exit 0
            fi
        fi
    done
    echo " Could not find JavaFX. Please install JavaFX and update the path in this script."
fi
