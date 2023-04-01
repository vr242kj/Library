package service;

import dao.BookDaoImplementation;
import dao.ReaderDaoImplementation;
import entity.Book;
import entity.Reader;

import java.util.List;
import java.util.Optional;

public class LibraryService {

    private final String JUST_NUMBER = "\nInput must be numeric";
    private final String ILLEGAL_ARGUMENT_FOR_RETURN_BOOK = "Book ID must be numeric";
    private final String ILLEGAL_ARGUMENT_FOR_ADD_BOOK = "Try again like this: name / author";
    private final String ILLEGAL_ARGUMENT_FOR_ADD_READER = "Full name must be literal";

    private final String BOOK_NOT_EXIST = "This book id doesn't exist";
    private final String READER_NOT_EXIST = "This reader id doesn't exist";

    private final BookDaoImplementation bookDaoImplementation = new BookDaoImplementation();
    private final ReaderDaoImplementation readerDaoImplementation = new ReaderDaoImplementation();

   public Optional<Reader> currentReaderOfBook(String bookId) throws IllegalArgumentException {
        if (!bookId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(JUST_NUMBER);
        }

        int bookID = Integer.parseInt(bookId);

       bookDaoImplementation.findById(bookID)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_EXIST));

        int userID = bookDaoImplementation.findReaderIdByBookId(bookID);

        if (userID == 0) {
            return Optional.empty();
        }

        return readerDaoImplementation.findById(userID);
    }

    public List<Book> allBorrowedBookByReaderId(String readerId) throws IllegalArgumentException {
        if (!readerId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(JUST_NUMBER);
        }

        int readerID = Integer.parseInt(readerId);

        readerDaoImplementation.findById(readerID)
                .orElseThrow(() -> new IllegalArgumentException(READER_NOT_EXIST));

        return bookDaoImplementation.findAllByReaderId(readerID);
    }

    public void returnBook(String bookId) throws IllegalArgumentException {
        if (!bookId.matches("^\\s*\\d+\\s*$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }

        int bookID = Integer.parseInt(bookId);

        bookDaoImplementation.findById(bookID)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_EXIST));

        bookDaoImplementation.returnBookToLibrary(bookID);
    }



    public void borrowBookToReader(String inputBookIDAndReaderID) throws IllegalArgumentException {
        if (!inputBookIDAndReaderID.matches("^\\s*(\\d+)(\\s\\/\\s)(\\d+\\s*)$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }

        String[] bookIdAndAuthorId = inputBookIDAndReaderID.split("/");
        int bookID = Integer.parseInt(bookIdAndAuthorId[0].trim());
        int readerID = Integer.parseInt(bookIdAndAuthorId[1].trim());

        if (bookDaoImplementation.findById(readerID).isEmpty()) {
            throw new IllegalArgumentException(BOOK_NOT_EXIST);
        }

        if (readerDaoImplementation.findById(bookID).isEmpty()) {
            throw new IllegalArgumentException(READER_NOT_EXIST);
        }

        bookDaoImplementation.borrowBookToReader(bookID, readerID);
    }


    public void addNewBook(String inputNameAndAuthor) throws IllegalArgumentException {
        if (!inputNameAndAuthor.matches("^\\s*\\D([a-zA-Z]+)(\\s\\/\\s)([a-zA-Z]+\\s*)$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_ADD_BOOK);
        }

        String[] nameAndAuthor = inputNameAndAuthor.split("/");
        bookDaoImplementation.save(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
    }

    public void addNewReader(String fullName) throws IllegalArgumentException {
        if (!fullName.matches("^[a-zA-Z]+\\s*[a-zA-Z]+$")) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_FOR_ADD_READER);
        }

        readerDaoImplementation.save(new Reader(fullName));
    }

    public List<Book> findAllBooks () {
        return bookDaoImplementation.findAll();
    }

    public List<Reader> findAllReaders () {
        return readerDaoImplementation.findAll();
    }
}
