ALTER TABLE book
    DROP COLUMN readerId;

ALTER TABLE book
    ADD COLUMN maxBorrowTimeInDays INT NOT NULL DEFAULT 14;

ALTER TABLE book
    ADD COLUMN restricted BOOLEAN NOT NULL DEFAULT false;