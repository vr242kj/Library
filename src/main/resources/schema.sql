DROP TABLE IF EXISTS reader CASCADE;

CREATE TABLE reader (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS book;

CREATE TABLE book (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       author VARCHAR(50) NOT NULL,
                       readerId INT REFERENCES reader ON DELETE RESTRICT
);
