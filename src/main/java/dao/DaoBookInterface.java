package dao;

import entity.Book;

import java.util.List;
import java.util.Optional;

public interface DaoBookInterface {
    List<Book> findAll ();
    Optional<Book> findById (int id);
    List<Book> findAllByReaderId (int readerId);
    Optional<Book> save (Book bookToSave);
    void borrowBookToReader (int bookId, int readerId);
    void returnBookToLibrary(int bookId);
    Integer findReaderIdByBookId (int bookId);
}
