package zd.DAO;

import zd.model.Book;
import zd.model.Reader;

import java.util.*;
import java.util.stream.Collectors;

public class Dao {

    public List<Book> books = new ArrayList<Book>();
    public List<Reader> readers = new ArrayList<Reader>();
    public HashMap<String, String> bookAndReader = new HashMap<String, String>();

    public Dao(){
        books.add(new Book("In Search of Lost Time", "Marcel Proust"));
        books.add(new Book( " Ulysses", "James Joyce"));
        books.add(new Book( " Don Quixote", "Miguel de Cervantes"));

        readers.add(new Reader( "Gabriel Garcia Marquez"));
        readers.add(new Reader( "F. Scott Fitzgerald"));
        readers.add(new Reader( "Herman Melville"));
    }

    public boolean isExistReaderById (String readerId) {
        return readers.stream().anyMatch(x -> x.getId() == Integer.parseInt(readerId));
    }

    public boolean isExistBookById (String bookId) {
        return books.stream().anyMatch(x -> x.getId() == Integer.parseInt(bookId));
    }

    public Reader getReaderById(String readerId) {
        return readers.stream().filter(x -> x.getId() == Integer.parseInt(readerId)).findFirst().get();
    }

    public Reader getCurrentReaderOfBook (String bookId) {
        String readerId = bookAndReader.entrySet()
                .stream()
                .filter(x -> x.getKey().equals(bookId))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");

        return readerId.isEmpty() ? null : getReaderById(readerId);
    }



    public List<Book> getAllBorrowedBookByReaderId (String readerId) {

        List<String> bookIds = bookAndReader.entrySet()
                .stream()
                .filter(x -> x.getValue().equals(readerId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return bookIds.isEmpty() ? null : getBookById(bookIds);
    }

    public List<Book> getBookById (List<String> bookIds) {
        return books.stream().filter(x -> bookIds.contains(String.valueOf(x.getId()))).collect(Collectors.toList());
    }
}
