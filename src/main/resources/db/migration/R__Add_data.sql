INSERT INTO reader(name)
SELECT 'Gabriel Garcia Marquez' WHERE NOT EXISTS (SELECT 1 FROM reader WHERE name = 'F. Scott Fitzgerald');
INSERT INTO reader(name)
SELECT 'F. Scott Fitzgerald' WHERE NOT EXISTS (SELECT 1 FROM reader WHERE name = 'Herman Melville');
INSERT INTO reader(name)
SELECT 'Herman Melville' WHERE NOT EXISTS (SELECT 1 FROM reader WHERE name = 'Herman Melville');

INSERT INTO book(name, author)
SELECT 'In Search of Lost Time', 'Marcel Proust' WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'In Search of Lost Time');
INSERT INTO book(name, author)
SELECT 'Ulysses', 'James Joyce' WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Ulysses');
INSERT INTO book(name, author)
SELECT 'Don Quixote', 'Miguel de Cervantes' WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Don Quixote');
INSERT INTO book(name, author)
SELECT 'Moby Dick', 'Herman Melville' WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Moby Dick');
INSERT INTO book(name, author)
SELECT 'Hamlet', 'William Shakespeare' WHERE NOT EXISTS (SELECT 1 FROM book WHERE name = 'Hamlet');

UPDATE book SET readerId = 1 WHERE id = 1;
UPDATE book SET readerId = 3 WHERE id = 2;
UPDATE book SET readerId = 3 WHERE id = 3;
