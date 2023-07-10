INSERT INTO reader(name) VALUES ('Gabriel Garcia Marquez'),
                                ('F. Scott Fitzgerald'),
                                ('Herman Melville')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO book(name, author) VALUES ('In Search of Lost Time', 'Marcel Proust'),
                                ('Ulysses', 'James Joyce'),
                                ('Don Quixote', 'Miguel de Cervantes'),
                                ('Moby Dick', 'Herman Melville'),
                                ('Hamlet', 'William Shakespeare')
    ON CONFLICT (id) DO NOTHING;

UPDATE book SET readerId = 1 WHERE id = 1;
UPDATE book SET readerId = 3 WHERE id = 2;
UPDATE book SET readerId = 3 WHERE id = 3;
