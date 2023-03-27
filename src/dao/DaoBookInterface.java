package dao;

import entity.Book;

import java.util.List;
import java.util.Optional;

public interface DaoBookInterface {
    List<Book> findAll ();
    Optional<Book> findById (long id);
    List<Book> findAllBorrowedBookByReaderId (long readerId);
    Book save (Book bookToSave);
    void borrowBookToReader (long bookId, long readerId);
    void returnBookToLibrary(long bookId);
    Long findCurrentReaderOfBook (long bookId);
}
