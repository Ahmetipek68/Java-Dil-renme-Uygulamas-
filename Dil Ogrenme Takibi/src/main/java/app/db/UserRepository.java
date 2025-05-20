package app.db;

import app.config.DatabaseConfig;
import app.model.User;
import java.sql.*;

public class UserRepository {
    public boolean register(User user) {
        try (Connection conn = DatabaseConfig.getMySQLConnection()) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Hatanın detayını konsola yazdır!
            return false;
        }
    }

    public User login(String username, String password) {
        try (Connection conn = DatabaseConfig.getMySQLConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByUsername(String username) {
        try (Connection conn = DatabaseConfig.getMySQLConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
} 