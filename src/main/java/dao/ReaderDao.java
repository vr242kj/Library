package dao;

import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReaderDao {
    List<Reader> findAll ();
    Optional<Reader> findById (long id);
    Reader save (Reader readerToSave);
    Optional<Reader> findByBookId(long bookId);
    Map<Reader, List<Book>> findAllWithBooks();
}