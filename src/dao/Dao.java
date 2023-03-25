package dao;

import entity.Book;
import entity.Reader;

import java.util.*;
import java.util.stream.Collectors;

public class Dao {

    private List<Book> books = new ArrayList<Book>();
    private List<Reader> readers = new ArrayList<Reader>();
    private HashMap<Long, Long> booksAndReaders = new HashMap<Long, Long>();

    public Dao(){
        books.add(new Book("In Search of Lost Time", "Marcel Proust"));
        books.add(new Book( " Ulysses", "James Joyce"));
        books.add(new Book( " Don Quixote", "Miguel de Cervantes"));

        readers.add(new Reader( "Gabriel Garcia Marquez"));
        readers.add(new Reader( "F. Scott Fitzgerald"));
        readers.add(new Reader( "Herman Melville"));
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public HashMap<Long, Long> getBooksAndReaders() {
        return booksAndReaders;
    }

    public Optional<Reader> findCurrentReaderOfBook (long bookId) {
        return findReaderById(booksAndReaders.get(bookId));
    }

    public Optional<Reader> findReaderById(long readerId) {
        return readers.stream().filter(reader -> reader.getId() == readerId).findFirst();
    }

    public Optional<Book> findBookById(long bookId) {
        return books.stream().filter(book -> book.getId() == bookId).findFirst();
    }

    public List<Book> findAllBorrowedBookByReaderId (long readerId) {

        List<Long> bookIds = booksAndReaders.entrySet()
                .stream()
                .filter(bookAndReader -> bookAndReader.getValue().equals(readerId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return findListOfBooksByBookId(bookIds);
    }

    public List<Book> findListOfBooksByBookId (List<Long> bookIds) {
        return books.stream().filter(book -> bookIds.contains(book.getId())).collect(Collectors.toList());
    }
}
