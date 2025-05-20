package app.db;

import app.config.DatabaseConfig;
import java.sql.*;

public class ProgressRepository {
    public void saveProgress(int userId, int correct, int wrong) {
        try (Connection conn = DatabaseConfig.getMySQLConnection()) {
            String sql = "INSERT INTO progress (user_id, correct, wrong) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, correct);
            pstmt.setInt(3, wrong);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 