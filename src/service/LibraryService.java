package service;

import dao.Dao;

import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Optional;

public class LibraryService {

    private final String JUST_NUMBER = "\nIllegal Argument, try again (just number)";
    private final String ILLEGAL_ARGUMENT_FOR_RETURN_BOOK = "Illegal Argument, try again only number";
    private final String ILLEGAL_ARGUMENT_FOR_ADD_BOOK = "Illegal Argument, try again like this: name / author";
    private final String ILLEGAL_ARGUMENT_FOR_ADD_READER = "Illegal Argument, try again only letters";

    private final String BOOK_NOT_EXIST = "This book id doesn't exist";
    private final String READER_NOT_EXIST = "This reader id doesn't exist";
    private final String NOT_EXIST = "Book id or reader id doesn't exist";
    private final String READER_DIDNT_BORROW_BOOK = "This reader hasn't borrowed the book yet";

    private final Dao dao = new Dao();

    public Optional<Reader> currentReaderOfBook(String bookId) throws IllegalArgumentException {
        if (!bookId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(JUST_NUMBER);
        }

        long bookID = Long.parseLong(bookId);

        dao.findBookById(bookID)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_EXIST));

        return dao.findCurrentReaderOfBook(bookID);
    }

    public List<Book> allBorrowedBookByReaderId(String readerId) throws IllegalArgumentException {
        if (!readerId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(JUST_NUMBER);
        }

        long readerID = Long.parseLong(readerId);

        dao.findReaderById(readerID)
                .orElseThrow(() -> new IllegalArgumentException(READER_NOT_EXIST));

        if (dao.findAllBorrowedBookByReaderId(readerID).isEmpty()) {
            throw new IllegalArgumentException(READER_DIDNT_BORROW_BOOK);
        }

        return dao.findAllBorrowedBookByReaderId(readerID);
    }

    public Long returnBook(String bookId) throws IllegalArgumentException {
        if (!bookId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }

        long bookID = Long.parseLong(bookId);

        dao.findBookById(bookID)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_EXIST));

        return dao.getBooksAndReaders().remove(bookID);
    }



    public void borrowBookToReader(String inputBookIDAndReaderID) throws IllegalArgumentException {
        if (!inputBookIDAndReaderID.matches("^\\s*(\\d+)(\\s\\/\\s)(\\d+\\s*)$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }

        String[] bookIdAndAuthorId = inputBookIDAndReaderID.split("/");
        long bookID = Long.parseLong(bookIdAndAuthorId[0].trim());
        long readerID = Long.parseLong(bookIdAndAuthorId[1].trim());

        if (dao.findReaderById(bookID).isEmpty() && dao.findReaderById(readerID).isEmpty()) {
            throw new IllegalArgumentException(NOT_EXIST);
        }

        dao.getBooksAndReaders().put(bookID, readerID);
    }


    public void addNewBook(String inputNameAndAuthor) throws IllegalArgumentException {
        if (!inputNameAndAuthor.matches("^\\s*\\D([a-zA-Z]+)(\\s\\/\\s)([a-zA-Z]+\\s*)$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_ADD_BOOK);
        }

        String[] nameAndAuthor = inputNameAndAuthor.split("/");
        dao.getBooks().add(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
    }

    public void addNewReader(String fullName) throws IllegalArgumentException {
        if (!fullName.matches("^[a-zA-Z]+\\s*[a-zA-Z]+$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_ADD_READER);
        }

        dao.getReaders().add(new Reader(fullName));
    }

    public List<Book> findAllBooks () {
        return dao.getBooks();
    }

    public List<Reader> findAllReaders () {
        return dao.getReaders();
    }
}
