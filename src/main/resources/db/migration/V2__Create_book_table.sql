CREATE TABLE IF NOT EXISTS book (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      author VARCHAR(50) NOT NULL,
                      readerId INT REFERENCES reader ON DELETE RESTRICT
);
