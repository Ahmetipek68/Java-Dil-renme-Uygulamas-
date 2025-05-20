package app.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {
    private static final String MONGODB_URI = "mongodb://localhost:27017";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/dil_ogrenme_takibi";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "root";

    public static MongoClient getMongoClient() {
        return MongoClients.create(MONGODB_URI);
    }

    public static Connection getMySQLConnection() throws Exception {
        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }
} 