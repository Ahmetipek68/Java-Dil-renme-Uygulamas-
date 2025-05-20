CREATE TABLE IF NOT EXISTS progress (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    quiz_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    correct INT NOT NULL,
    wrong INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
); 