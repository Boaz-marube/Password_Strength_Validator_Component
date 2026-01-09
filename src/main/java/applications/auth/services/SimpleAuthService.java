package applications.auth.services;

import applications.auth.models.User;
import java.io.*;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

public class SimpleAuthService {
    private static final String USERS_FILE = "users.txt";
    private Map<String, User> users;

    public SimpleAuthService() {
        users = new HashMap<>();
        loadUsers();
    }

    public boolean registerUser(String username, String email, String password) {
        if (emailExists(email)) {
            return false;
        }

        String hashedPassword = hashPassword(password);
        User user = new User(username, email, hashedPassword);
        user.setId(users.size() + 1);
        
        users.put(email, user);
        saveUsers();
        return true;
    }

    public User loginUser(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && verifyPassword(password, user.getPasswordHash())) {
                user.setLastLogin(LocalDateTime.now());
                saveUsers();
                return user;
            }
        }
        return null;
    }

    public boolean emailExists(String email) {
        return users.containsKey(email);
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    User user = new User();
                    user.setId(Integer.parseInt(parts[0]));
                    user.setUsername(parts[1]);
                    user.setEmail(parts[2]);
                    user.setPasswordHash(parts[3]);
                    if (parts.length > 4) {
                        user.setCreatedAt(LocalDateTime.parse(parts[4]));
                    }
                    if (parts.length > 5 && !parts[5].isEmpty()) {
                        user.setLastLogin(LocalDateTime.parse(parts[5]));
                    }
                    users.put(user.getEmail(), user);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet, start with empty users
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                writer.println(user.getId() + "|" + user.getUsername() + "|" + user.getEmail() + "|" + 
                             user.getPasswordHash() + "|" + user.getCreatedAt() + "|" + 
                             (user.getLastLogin() != null ? user.getLastLogin() : ""));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users: " + e.getMessage());
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String salt = generateSalt();
            md.update((password + salt).getBytes());
            byte[] hash = md.digest();
            return bytesToHex(hash) + ":" + salt;
        } catch (Exception e) {
            throw new RuntimeException("Password hashing failed");
        }
    }

    private boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            String hash = parts[0];
            String salt = parts[1];
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((password + salt).getBytes());
            byte[] testHash = md.digest();
            
            return hash.equals(bytesToHex(testHash));
        } catch (Exception e) {
            return false;
        }
    }

    private String generateSalt() {
        Random random = new Random();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}