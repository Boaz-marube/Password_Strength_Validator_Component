package applications.auth.services;

import applications.auth.models.User;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Random;

public class AuthService {
    private static final String DB_URL = "jdbc:sqlite:users.db";
    private Connection connection;

    public AuthService() {
        initDatabase();
    }

    private void initDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                created_at TEXT NOT NULL,
                last_login TEXT
            )
        """;
        connection.createStatement().execute(createUsersTable);
    }

    public boolean registerUser(String username, String email, String password) throws SQLException {
        if (emailExists(email)) {
            return false;
        }

        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO users (username, email, password_hash, created_at) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, LocalDateTime.now().toString());
            stmt.executeUpdate();
            return true;
        }
    }

    public User loginUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && verifyPassword(password, rs.getString("password_hash"))) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                
                updateLastLogin(user.getId());
                return user;
            }
        }
        return null;
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt(1) > 0;
        }
    }

    private void updateLastLogin(int userId) throws SQLException {
        String sql = "UPDATE users SET last_login = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, LocalDateTime.now().toString());
            stmt.setInt(2, userId);
            stmt.executeUpdate();
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