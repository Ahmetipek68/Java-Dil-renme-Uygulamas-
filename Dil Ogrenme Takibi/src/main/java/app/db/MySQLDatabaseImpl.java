package app.db;

import app.model.Word;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLDatabaseImpl implements Database {
    private Connection conn;

    @Override
    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dil_ogrenme_takibi", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addWord(Word word) {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO words (word, meaning, language, difficulty, user_id) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, word.getWord());
            stmt.setString(2, word.getMeaning());
            stmt.setString(3, word.getLanguage());
            stmt.setInt(4, word.getDifficulty());
            stmt.setInt(5, word.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Word> getAllWords() {
        List<Word> words = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT word, meaning, language, difficulty, user_id FROM words")) {
            while (rs.next()) {
                words.add(new Word(
                    rs.getString("word"),
                    rs.getString("meaning"),
                    rs.getString("language"),
                    rs.getInt("difficulty"),
                    rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    @Override
    public List<Word> getWordsByLanguage(String language, int userId) {
        List<Word> words = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT word, meaning, language, difficulty, user_id FROM words WHERE language = ? AND user_id = ?")) {
            stmt.setString(1, language);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                words.add(new Word(
                    rs.getString("word"),
                    rs.getString("meaning"),
                    rs.getString("language"),
                    rs.getInt("difficulty"),
                    rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    @Override
    public List<Word> getRandomWordsForQuiz(String language, int count, int userId) {
        List<Word> words = getWordsByLanguage(language, userId);
        java.util.Collections.shuffle(words);
        if (words.size() > count) {
            return words.subList(0, count);
        }
        return words;
    }
}