package app.db;

import app.model.Word;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDatabaseImpl implements Database {
    private MongoCollection<Document> collection;

    @Override
    public void connect() {
        MongoClient client = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = client.getDatabase("dil");
        collection = db.getCollection("words");
    }

    @Override
    public void addWord(Word word) {
        Document doc = new Document("word", word.getWord())
                .append("meaning", word.getMeaning())
                .append("language", word.getLanguage())
                .append("difficulty", word.getDifficulty());
        collection.insertOne(doc);
    }

    @Override
    public List<Word> getAllWords() {
        List<Word> words = new ArrayList<>();
        for (Document doc : collection.find()) {
            words.add(new Word(
                doc.getString("word"),
                doc.getString("meaning"),
                doc.getString("language"),
                doc.getInteger("difficulty"),
                doc.get("user_id") != null ? doc.getInteger("user_id") : 0
            ));
        }
        return words;
    }

    @Override
    public List<Word> getWordsByLanguage(String language, int userId) {
        List<Word> words = new ArrayList<>();
        for (Document doc : collection.find(new org.bson.Document("language", language))) {
            words.add(new Word(
                doc.getString("word"),
                doc.getString("meaning"),
                doc.getString("language"),
                doc.getInteger("difficulty"),
                doc.get("user_id") != null ? doc.getInteger("user_id") : 0
            ));
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