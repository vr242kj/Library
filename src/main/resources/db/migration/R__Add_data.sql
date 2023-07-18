INSERT INTO reader(name, birthdate)
SELECT 'Gabriel Garcia Marquez', '2022-01-01' WHERE NOT EXISTS (SELECT 1 FROM reader WHERE name = 'F. Scott Fitzgerald');
INSERT INTO reader(name, birthdate)
SELECT 'F. Scott Fitzgerald', '1999-01-01' WHERE NOT EXISTS (SELECT 1 FROM reader WHERE name = 'Herman Melville');
INSERT INTO reader(name, birthdate)
SELECT 'Herman Melville', '1980-01-01' WHERE NOT EXISTS (SELECT 1 FROM reader WHERE name = 'Herman Melville');

INSERT INTO book(name, author, maxborrowtimeindays, restricted)
SELECT 'In Search of Lost Time', 'Marcel Proust', 5, true WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'In Search of Lost Time');
INSERT INTO book(name, author, maxborrowtimeindays, restricted)
SELECT 'Ulysses', 'James Joyce', 10, true WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Ulysses');
INSERT INTO book(name, author, maxborrowtimeindays, restricted)
SELECT 'Don Quixote', 'Miguel de Cervantes', 14, false WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Don Quixote');
INSERT INTO book(name, author, maxborrowtimeindays, restricted)
SELECT 'Moby Dick', 'Herman Melville', 5, false WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Moby Dick');
INSERT INTO book(name, author, maxborrowtimeindays, restricted)
SELECT 'Hamlet', 'William Shakespeare', 10, true WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Hamlet');
