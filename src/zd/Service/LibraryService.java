package zd.Service;

import zd.DAO.Dao;
import zd.model.Book;
import zd.model.Reader;

import java.util.*;

public class LibraryService {

    private Scanner input = new Scanner(System.in);
    private Dao dao = new Dao();

    public Reader getReaderById(String readerId) {
        return dao.readers.stream().filter(x -> x.getId() == Integer.parseInt(readerId)).findFirst().get();
    }

    public void currentReaderOfBook(String bookId) {
        dao.bookAndReader.entrySet().stream()
                .filter(x -> x.getKey().equals(bookId))
                .forEach(x -> System.out.println(getReaderById(x.getValue())));
    }

    public Book getBookByReaderId (String bookId) {
        return dao.books.stream().filter(x -> x.getId() == Integer.parseInt(bookId)).findFirst().get();
    }

    public void allBorrowedByReaderId (String readerId) {
        dao.bookAndReader.entrySet().stream()
                .filter(x -> x.getValue().equals(readerId))
                .forEach(x -> System.out.println(getBookByReaderId(x.getKey()) + " "));
    }

    public void returnBook (String bookId) {
        dao.bookAndReader.remove(bookId);
    }

    public Map<String, String> borrowBookToReader (String[] bookIdAndAuthorId) {
        dao.bookAndReader.put(bookIdAndAuthorId[0].trim(), bookIdAndAuthorId[1].trim());
        return dao.bookAndReader;
    }


    public void addNewBook (String[] nameAndAuthor) {
        dao.books.add(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
    }

    public void addNewReader (String fullName) {
        dao.readers.add(new Reader(fullName));
    }

    public void printBookAndReader () {
        dao.bookAndReader.entrySet()
                .forEach(System.out::println);
    }

    public void printAllBooks () {
        dao.books.stream()
                .forEach(System.out::println);
    }

    public void printAllReaders () {
        dao.readers.stream()
                .forEach(System.out::println);
    }
}
