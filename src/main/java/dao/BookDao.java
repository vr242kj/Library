package dao;

import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookDao {
    List<Book> findAll ();
    Optional<Book> findById (long id);
    List<Book> findAllByReaderId (long readerId);
    Book save (Book bookToSave);
    void borrowBookToReader (long bookId, long readerId);
    void returnBookToLibrary(long bookId);
    Map<Book, Optional<Reader>> findAllWithReaders();
}