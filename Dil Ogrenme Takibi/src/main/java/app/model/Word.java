package app.model;

public class Word {
    private String id;
    private String word;
    private String meaning;
    private String language;
    private int difficulty;
    private int userId;

    public Word(String word, String meaning, String language, int difficulty, int userId) {
        this.word = word;
        this.meaning = meaning;
        this.language = language;
        this.difficulty = difficulty;
        this.userId = userId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}