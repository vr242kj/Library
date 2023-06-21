DROP TABLE IF EXISTS book;

CREATE TABLE book
(
    id       INT PRIMARY KEY,
    name     VARCHAR(255),
    author   VARCHAR(255),
    readerId INT
);

INSERT INTO book(id, name, author, readerId)
VALUES (1, 'In Search of Lost Time', 'Marcel Proust', 1),
       (2, 'Ulysses', 'James Joyce', 2),
       (3, 'Don Quixote', 'Miguel de Cervantes', 3);
