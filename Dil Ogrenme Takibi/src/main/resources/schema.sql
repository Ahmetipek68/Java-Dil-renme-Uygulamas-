CREATE DATABASE IF NOT EXISTS dil_ogrenme;
USE dil_ogrenme;

CREATE TABLE IF NOT EXISTS words (
    id INT AUTO_INCREMENT PRIMARY KEY,
    word VARCHAR(255) NOT NULL,
    meaning VARCHAR(255) NOT NULL,
    language VARCHAR(50) NOT NULL,
    difficulty INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 