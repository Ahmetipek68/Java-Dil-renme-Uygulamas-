package app;

import app.config.Env;
import app.db.*;
import app.model.Word;
import app.model.User;
import app.quiz.QuizManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Database db;

        String dbType = Env.get("DB_TYPE");
        if ("MONGODB".equalsIgnoreCase(dbType)) {
            db = new MongoDatabaseImpl();
        } else {
            db = new MySQLDatabaseImpl();
        }

        db.connect();

        UserRepository userRepository = new UserRepository();
        ProgressRepository progressRepository = new ProgressRepository();
        User currentUser = null;
        Scanner scanner = new Scanner(System.in);
        // Giriş/Kayıt Menüsü
        while (currentUser == null) {
            System.out.println("1. Giriş Yap");
            System.out.println("2. Kayıt Ol");
            System.out.print("Seçiminiz: ");
            int secim = Integer.parseInt(scanner.nextLine());
            if (secim == 1) {
                System.out.print("Kullanıcı adı: ");
                String username = scanner.nextLine();
                System.out.print("Şifre: ");
                String password = scanner.nextLine();
                User user = userRepository.login(username, password);
                if (user != null) {
                    System.out.println("Giriş başarılı!");
                    currentUser = user;
                } else {
                    System.out.println("Hatalı kullanıcı adı veya şifre!");
                }
            } else if (secim == 2) {
                System.out.print("Kullanıcı adı: ");
                String username = scanner.nextLine();
                System.out.print("Şifre: ");
                String password = scanner.nextLine();
                boolean success = userRepository.register(new User(username, password));
                if (success) {
                    System.out.println("Kayıt başarılı! Şimdi giriş yapabilirsiniz.");
                } else {
                    System.out.println("Kayıt başarısız! Kullanıcı adı alınmış olabilir.");
                }
            }
        }
        // Ana Menü
        while (true) {
            System.out.println("\n==============================");
            System.out.println("      DİL ÖĞRENME UYGULAMASI");
            System.out.println("==============================");
            System.out.println("\n  1. Yeni Kelime Ekle");
            System.out.println("  2. Quiz Başlat");
            System.out.println("  3. Kelimeleri Listele");
            System.out.println("  0. Çıkış");
            System.out.println("------------------------------");
            System.out.print("Seçiminiz: ");
            int secim = Integer.parseInt(scanner.nextLine());
            switch (secim) {
                case 1:
                    System.out.print("Dil: ");
                    String language = scanner.nextLine();
                    System.out.print("Kelime: ");
                    String word = scanner.nextLine();
                    System.out.print("Anlamı: ");
                    String meaning = scanner.nextLine();
                    System.out.print("Zorluk (1-5): ");
                    int difficulty = Integer.parseInt(scanner.nextLine());
                    db.addWord(new Word(word, meaning, language, difficulty, currentUser.getId()));
                    System.out.println("\u2705 Kelime başarıyla eklendi!");
                    System.out.println("\nDevam etmek için Enter'a basın...");
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.print("Dil: ");
                    String quizLang = scanner.nextLine();
                    System.out.print("Kaç soru olsun?: ");
                    int quizCount = Integer.parseInt(scanner.nextLine());
                    List<Word> quizWords = db.getRandomWordsForQuiz(quizLang, quizCount, currentUser.getId());
                    int dogru = 0, yanlis = 0;
                    for (Word qWord : quizWords) {
                        System.out.println("\nSoru: \"" + qWord.getWord() + "\" ne demek?");
                        System.out.print("Cevabınız: ");
                        String answer = scanner.nextLine();
                        if (qWord.getMeaning().equalsIgnoreCase(answer.trim())) {
                            System.out.println("\u2705 Doğru!");
                            dogru++;
                        } else {
                            System.out.println("\u274C Yanlış! Doğru cevap: " + qWord.getMeaning());
                            yanlis++;
                        }
                    }
                    progressRepository.saveProgress(currentUser.getId(), dogru, yanlis);
                    System.out.println("\nQuiz bitti! Doğru: " + dogru + " | Yanlış: " + yanlis);
                    System.out.println("\nDevam etmek için Enter'a basın...");
                    scanner.nextLine();
                    break;
                case 3:
                    List<Word> words = db.getAllWords();
                    boolean found = false;
                    for (Word w : words) {
                        if (w.getUserId() == currentUser.getId()) {
                            if (!found) {
                                System.out.println("\n--- Kayıtlı Kelimeleriniz ---");
                                found = true;
                            }
                            System.out.println("Kelime: " + w.getWord() + " | Anlamı: " + w.getMeaning() + " | Dil: " + w.getLanguage() + " | Zorluk: " + w.getDifficulty());
                        }
                    }
                    if (!found) {
                        System.out.println("Hiç kelimeniz yok.");
                    }
                    System.out.println("\nDevam etmek için Enter'a basın...");
                    scanner.nextLine();
                    break;
                case 0:
                    System.out.println("Çıkılıyor...");
                    return;
                default:
                    System.out.println("Geçersiz seçim.");
            }
        }
    }
}