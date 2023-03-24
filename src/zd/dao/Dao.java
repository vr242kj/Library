package zd.dao;

import zd.model.Book;
import zd.model.Reader;

import java.util.*;
import java.util.stream.Collectors;

public class Dao {

    public List<Book> books = new ArrayList<Book>();
    public List<Reader> readers = new ArrayList<Reader>();
    public HashMap<Long, Long> booksAndReaders = new HashMap<Long, Long>();

    public Dao(){
        books.add(new Book("In Search of Lost Time", "Marcel Proust"));
        books.add(new Book( " Ulysses", "James Joyce"));
        books.add(new Book( " Don Quixote", "Miguel de Cervantes"));

        readers.add(new Reader( "Gabriel Garcia Marquez"));
        readers.add(new Reader( "F. Scott Fitzgerald"));
        readers.add(new Reader( "Herman Melville"));
    }

    public boolean isExistReaderById (long readerId) {
        return readers.stream().anyMatch(reader -> reader.getId() == readerId);
    }

    public boolean isExistBookById (long bookId) {
        return books.stream().anyMatch(book -> book.getId() == bookId);
    }

    public Optional<Reader> getCurrentReaderOfBook (long bookId) {
        long readerId = booksAndReaders.entrySet()
                .stream()
                .filter(bookAndReader -> bookAndReader.getKey().equals(bookId))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0L);

        return getReaderById(readerId);
    }

    public Optional<Reader> getReaderById(long readerId) {
        return readers.stream().filter(reader -> reader.getId() == readerId).findFirst();
    }

    public List<Book> getAllBorrowedBookByReaderId (long readerId) {

        List<Long> bookIds = booksAndReaders.entrySet()
                .stream()
                .filter(bookAndReader -> bookAndReader.getValue().equals(readerId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return getBookById(bookIds);
    }

    public List<Book> getBookById (List<Long> bookIds) {
        return books.stream().filter(book -> bookIds.contains(book.getId())).collect(Collectors.toList());
    }
}
