DROP TABLE IF EXISTS reader;

CREATE TABLE reader (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(25) NOT NULL
);

DROP TABLE IF EXISTS book;

CREATE TABLE book (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       author VARCHAR(25) NOT NULL,
                       readerId INT REFERENCES reader ON DELETE RESTRICT
);
