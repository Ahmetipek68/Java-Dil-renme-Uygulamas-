package app.config;

import java.io.InputStream;
import java.util.Properties;

public class Env {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = Env.class.getClassLoader().getResourceAsStream(".env")) {
            if (input != null) {
                props.load(input);
            } else {
                System.out.println(".env dosyası bulunamadı.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}