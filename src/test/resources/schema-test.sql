DROP TABLE IF EXISTS reader CASCADE;

CREATE TABLE reader
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS book;

CREATE TABLE book
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    author   VARCHAR(50) NOT NULL,
    readerId INT,
    FOREIGN KEY (readerId) REFERENCES reader (id) ON DELETE RESTRICT
);

INSERT INTO reader(name)
VALUES ('Gabriel Garcia Marquez'),
       ('F. Scott Fitzgerald'),
       ('Herman Melville');

INSERT INTO book(name, author)
VALUES ('In Search of Lost Time', 'Marcel Proust'),
       ('Ulysses', 'James Joyce'),
       ('Don Quixote', 'Miguel de Cervantes');
