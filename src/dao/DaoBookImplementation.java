package dao;

import entity.Book;

import java.util.*;
import java.util.stream.Collectors;

public class DaoBookImplementation implements DaoBookInterface {
    private final List<Book> books = new ArrayList<Book>();
    private final HashMap<Long, Long> booksAndReaders = new HashMap<Long, Long>();

    public DaoBookImplementation() {
        save(new Book("In Search of Lost Time", "Marcel Proust"));
        save(new Book( " Ulysses", "James Joyce"));
        save(new Book( " Don Quixote", "Miguel de Cervantes"));
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    @Override
    public List<Book> findAllByReaderId(long readerId) {
        List<Long> bookIds = booksAndReaders.entrySet()
                .stream()
                .filter(bookAndReader -> bookAndReader.getValue().equals(readerId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return findAllByIds(bookIds);
    }

    private List<Book> findAllByIds (List<Long> bookIds) {
        return books.stream().filter(book -> bookIds.contains(book.getId())).collect(Collectors.toList());
    }

    @Override
    public Book save(Book bookToSave) {
        books.add(bookToSave);
        return bookToSave;
    }

    @Override
    public void borrowBookToReader(long bookId, long readerId) {
        booksAndReaders.put(bookId, readerId);
    }

    @Override
    public void returnBookToLibrary(long bookId) {
        booksAndReaders.remove(bookId);
    }

    @Override
    public Long findReaderIdByBookId(long bookId) {
        return booksAndReaders.get(bookId);
    }
}
