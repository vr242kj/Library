CREATE TABLE IF NOT EXISTS borrow (
    id SERIAL PRIMARY KEY,
    bookId INT REFERENCES book ON DELETE RESTRICT,
    readerId INT REFERENCES reader ON DELETE RESTRICT,
    borrowStartDate DATE NOT NULL,
    borrowEndDate DATE
);

ALTER TABLE book
    DROP COLUMN IF EXISTS readerId;

ALTER TABLE book
    ADD COLUMN if not exists maxBorrowTimeInDays INT NOT NULL DEFAULT 14;

ALTER TABLE book
    ADD COLUMN if not exists restricted BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE reader
    ADD COLUMN if not exists birthDate DATE NOT NULL DEFAULT '1900-01-01';

