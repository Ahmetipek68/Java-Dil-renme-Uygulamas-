package app.quiz;

import app.model.Word;
import app.db.WordRepository;
import java.util.*;

public class QuizManager {
    private final WordRepository wordRepository;
    private List<Word> currentQuizWords;
    private int currentIndex;
    private int correctAnswers;

    public QuizManager(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
        this.currentIndex = 0;
        this.correctAnswers = 0;
    }

    public void startNewQuiz(String language, int wordCount) {
        currentQuizWords = wordRepository.getRandomWordsForQuiz(language, wordCount);
        Collections.shuffle(currentQuizWords);
        currentIndex = 0;
        correctAnswers = 0;
    }

    public Word getCurrentWord() {
        if (currentIndex < currentQuizWords.size()) {
            return currentQuizWords.get(currentIndex);
        }
        return null;
    }

    public boolean checkAnswer(String answer) {
        Word currentWord = getCurrentWord();
        if (currentWord != null) {
            boolean isCorrect = currentWord.getMeaning().equalsIgnoreCase(answer.trim());
            if (isCorrect) {
                correctAnswers++;
            }
            currentIndex++;
            return isCorrect;
        }
        return false;
    }

    public boolean isQuizFinished() {
        return currentIndex >= currentQuizWords.size();
    }

    public int getScore() {
        return correctAnswers;
    }

    public int getTotalQuestions() {
        return currentQuizWords.size();
    }
} 