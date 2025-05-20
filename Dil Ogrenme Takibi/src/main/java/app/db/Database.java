package app.db;

import app.model.Word;
import java.util.List;

public interface Database {
    void connect();
    void addWord(Word word);
    List<Word> getAllWords();
    List<Word> getWordsByLanguage(String language, int userId);
    List<Word> getRandomWordsForQuiz(String language, int count, int userId);
}