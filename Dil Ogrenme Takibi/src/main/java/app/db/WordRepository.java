package app.db;

import app.config.DatabaseConfig;
import app.model.Word;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WordRepository {
    private final MongoCollection<Document> mongoCollection;
    private final String tableName = "words";

    public WordRepository() {
        MongoClient mongoClient = DatabaseConfig.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("dil_ogrenme_takibi");
        mongoCollection = database.getCollection("words");
    }

    public void saveWord(Word word) {
        // MongoDB'ye kaydet
        Document doc = new Document()
            .append("word", word.getWord())
            .append("meaning", word.getMeaning())
            .append("language", word.getLanguage())
            .append("difficulty", word.getDifficulty());
        mongoCollection.insertOne(doc);

        // MySQL'e kaydet
        try (Connection conn = DatabaseConfig.getMySQLConnection()) {
            String sql = "INSERT INTO " + tableName + " (word, meaning, language, difficulty) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word.getWord());
            pstmt.setString(2, word.getMeaning());
            pstmt.setString(3, word.getLanguage());
            pstmt.setInt(4, word.getDifficulty());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Word> getWordsByLanguage(String language) {
        List<Word> words = new ArrayList<>();
        
        // MongoDB'den al
        FindIterable<Document> documents = mongoCollection.find(Filters.eq("language", language));
        for (Document doc : documents) {
            Word word = new Word(
                doc.getString("word"),
                doc.getString("meaning"),
                doc.getString("language"),
                doc.getInteger("difficulty"),
                doc.get("user_id") != null ? doc.getInteger("user_id") : 0
            );
            word.setId(doc.getObjectId("_id").toString());
            words.add(word);
        }

        return words;
    }

    public List<Word> getRandomWordsForQuiz(String language, int count) {
        List<Word> words = new ArrayList<>();
        
        // MongoDB'den rastgele kelimeler al
        FindIterable<Document> documents = mongoCollection.find(Filters.eq("language", language))
            .limit(count);
        for (Document doc : documents) {
            Word word = new Word(
                doc.getString("word"),
                doc.getString("meaning"),
                doc.getString("language"),
                doc.getInteger("difficulty"),
                doc.get("user_id") != null ? doc.getInteger("user_id") : 0
            );
            word.setId(doc.getObjectId("_id").toString());
            words.add(word);
        }

        return words;
    }
} 