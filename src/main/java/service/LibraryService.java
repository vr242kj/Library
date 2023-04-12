package service;

import dao.DaoBookImplementation;
import dao.DaoBookInterface;
import dao.DaoReaderInterface;
import dao.DaoReaderImplementation;
import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LibraryService {
    private final DaoBookInterface daoBookImplementation = new DaoBookImplementation();
    private final DaoReaderInterface daoReaderImplementation = new DaoReaderImplementation();

   public Optional<Reader> currentReaderOfBook (String bookId) {
       long bookIdNumeric = convertStringToLong(bookId);

       daoBookImplementation.findById(bookIdNumeric)
               .orElseThrow(() -> new ServiceException("This book id doesn't exist"));

       return daoReaderImplementation.findByBookId(bookIdNumeric);
    }

    public List<Book> allBorrowedBookByReaderId (String readerId) {
        long readerIdNumeric = convertStringToLong(readerId);

        daoReaderImplementation.findById(readerIdNumeric)
                .orElseThrow(() -> new ServiceException("This reader id doesn't exist"));

        return daoBookImplementation.findAllByReaderId(readerIdNumeric);
    }

    public void returnBook (String bookId) {
        long bookIdNumeric = convertStringToLong(bookId);

        daoBookImplementation.findById(bookIdNumeric)
                .orElseThrow(() -> new ServiceException("This book id doesn't exist"));

        daoBookImplementation.returnBookToLibrary(bookIdNumeric);
    }

    public void borrowBookToReader (String inputBookIDAndReaderID) {
        if (!inputBookIDAndReaderID.matches("^\\s*\\d+\\/(\\d+\\s*)$")) {
            throw new ServiceException("Try again like this: book id/reader id. One slash, without spaces before and after. " +
                    "Book id and reader id must be numeric");
        }

        String[] bookIdAndReaderId = inputBookIDAndReaderID.split("/");

        long bookIdNumeric = convertStringToLong(bookIdAndReaderId[0].trim());
        long readerIdNumeric = convertStringToLong(bookIdAndReaderId[1].trim());

        if (daoBookImplementation.findById(bookIdNumeric).isEmpty()) {
            throw new ServiceException("This book id doesn't exist");
        }

        if (daoReaderImplementation.findById(readerIdNumeric).isEmpty()) {
            throw new ServiceException("This reader id doesn't exist");
        }

        daoBookImplementation.borrowBookToReader(bookIdNumeric, readerIdNumeric);
    }


    public void addNewBook (String inputNameAndAuthor) {
        if (!inputNameAndAuthor.matches("^[A-Za-z0-9\\s\\-_,\\.;:()]+(\\S\\/)([a-zA-Z]+\\s?[a-zA-Z]+\\s?[a-zA-Z]*\\s*)$")) {
            throw new ServiceException("Try again like this: name/author. One slash, without spaces before and after. Author must be literal");
        }

        String[] nameAndAuthor = inputNameAndAuthor.split("/");
        daoBookImplementation.save(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
    }

    public void addNewReader (String fullName) {
        if (!fullName.matches("^[a-zA-Z]+\\s?[a-zA-Z]+\\s?[a-zA-Z]*$")) {
            throw new ServiceException("Full name must be literal and one space between words (max 3 words)");
        }

        daoReaderImplementation.save(new Reader(fullName));
    }

    public List<Book> findAllBooks () {
        return daoBookImplementation.findAll();
    }

    public List<Reader> findAllReaders () {
        return daoReaderImplementation.findAll();
    }

    public Map<Reader, Optional<Book>> findAllReadersWithBorrowedBooks () {
        return daoReaderImplementation.findAllWithBooks();
    }

    public Map<Book, Optional<Reader>> findAllBooksWithReaders () {
        return daoBookImplementation.findAllWithReaders();
    }

    private long convertStringToLong (String id) {
        try{
            return Long.parseLong(id);
        }catch (NumberFormatException e) {
            throw new ServiceException("Id must be numeric");
        }
    }
}
