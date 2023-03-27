package service;

import dao.DaoBookImplementation;
import dao.DaoReaderImplementation;
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

    private final DaoBookImplementation daoBookImplementation = new DaoBookImplementation();
    private final DaoReaderImplementation daoReaderImplementation = new DaoReaderImplementation();

    public Optional<Reader> currentReaderOfBook(String bookId) throws IllegalArgumentException {
        if (!bookId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(JUST_NUMBER);
        }

        long bookID = Long.parseLong(bookId);

        daoBookImplementation.findById(bookID)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_EXIST));

        if (daoBookImplementation.findCurrentReaderOfBook(bookID) == null) {
            return Optional.empty();
        }

        return daoReaderImplementation.findById(daoBookImplementation.findCurrentReaderOfBook(bookID));
    }

    public List<Book> allBorrowedBookByReaderId(String readerId) throws IllegalArgumentException {
        if (!readerId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(JUST_NUMBER);
        }

        long readerID = Long.parseLong(readerId);

        daoReaderImplementation.findById(readerID)
                .orElseThrow(() -> new IllegalArgumentException(READER_NOT_EXIST));

        return daoBookImplementation.findAllBorrowedBookByReaderId(readerID);
    }

    public void returnBook(String bookId) throws IllegalArgumentException {
        if (!bookId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }

        long bookID = Long.parseLong(bookId);

        daoBookImplementation.findById(bookID)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_EXIST));

        daoBookImplementation.returnBookToLibrary(bookID);
    }



    public void borrowBookToReader(String inputBookIDAndReaderID) throws IllegalArgumentException {
        if (!inputBookIDAndReaderID.matches("^\\s*(\\d+)(\\s\\/\\s)(\\d+\\s*)$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }

        String[] bookIdAndAuthorId = inputBookIDAndReaderID.split("/");
        long bookID = Long.parseLong(bookIdAndAuthorId[0].trim());
        long readerID = Long.parseLong(bookIdAndAuthorId[1].trim());

        if (daoReaderImplementation.findById(bookID).isEmpty() && daoBookImplementation.findById(readerID).isEmpty()) {
            throw new IllegalArgumentException(NOT_EXIST);
        }

        daoBookImplementation.borrowBookToReader(bookID, readerID);
    }


    public void addNewBook(String inputNameAndAuthor) throws IllegalArgumentException {
        if (!inputNameAndAuthor.matches("^\\s*\\D([a-zA-Z]+)(\\s\\/\\s)([a-zA-Z]+\\s*)$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_ADD_BOOK);
        }

        String[] nameAndAuthor = inputNameAndAuthor.split("/");
        daoBookImplementation.save(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
    }

    public void addNewReader(String fullName) throws IllegalArgumentException {
        if (!fullName.matches("^[a-zA-Z]+\\s*[a-zA-Z]+$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_ADD_READER);
        }

        daoReaderImplementation.save(new Reader(fullName));
    }

    public List<Book> findAllBooks () {
        return daoBookImplementation.findAll();
    }

    public List<Reader> findAllReaders () {
        return daoReaderImplementation.findAll();
    }
}
