CREATE TABLE IF NOT EXISTS borrow (
    id SERIAL PRIMARY KEY,
    bookId INT REFERENCES book ON DELETE RESTRICT,
    readerId INT REFERENCES reader ON DELETE RESTRICT,
    borrowStartDate DATE NOT NULL,
    borrowEndDate DATE
)
