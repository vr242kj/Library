package dao;

import entity.Book;

import java.util.*;
import java.util.stream.Collectors;

public class DaoBookImplementation implements DaoBookInterface {
    private final List<Book> books = new ArrayList<Book>();

    public DaoBookImplementation() {
        //save(new Book("In Search of Lost Time", "Marcel Proust", 0));
        //save(new Book(" Ulysses", "James Joyce", 0));
        //save(new Book(" Don Quixote", "Miguel de Cervantes", 0));

    }

    @Override
    public List<Book> findAll () {
        return books;
    }

    @Override
    public Optional<Book> findById (int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst();
    }

    @Override
    public List<Book> findAllByReaderId (int readerId) {
        return books.stream()
                .filter(book -> book.getReaderId() == readerId)
                .collect(Collectors.toList());
    }

    @Override
    public Book save (Book bookToSave) {
        books.add(bookToSave);
        return bookToSave;
    }

    @Override
    public void borrowBookToReader (int bookId, int readerId) {
        findById(bookId).get().setReaderId(readerId);
    }

    @Override
    public void returnBookToLibrary (int bookId) {
        findById(bookId).get().setReaderId(0);
    }

    @Override
    public Integer findReaderIdByBookId (int bookId) {
        return findById(bookId).get().getReaderId();
    }
}
