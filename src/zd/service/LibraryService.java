package zd.service;

import zd.dao.Dao;
import zd.ui.UserInterface;

import zd.model.Book;
import zd.model.Reader;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LibraryService {

    private final Dao dao = new Dao();

    public Optional<Reader> currentReaderOfBook (String bookId) throws IllegalArgumentException{
        if (bookId.matches("^\\s*\\d+\\s*$")) {
            long bookID = Long.parseLong(bookId);
            if (dao.isExistBookById(bookID)) {
                if (dao.getCurrentReaderOfBook(bookID).isPresent()) {
                    return dao.getCurrentReaderOfBook(bookID);
                } else {
                    throw new IllegalArgumentException(UserInterface.BOOK_IN_LIBRARY);
                }
            } else {
                throw new IllegalArgumentException(UserInterface.BOOK_NOT_EXIST);
            }
        } else {
            throw new IllegalArgumentException(UserInterface.JUST_NUMBER);
        }
    }

    public List<Book> allBorrowedBookByReaderId (String readerId) throws IllegalArgumentException{
        if (readerId.matches("^\\s*\\d+\\s*$")) {
            long readerID = Long.parseLong(readerId);
            if (dao.isExistReaderById(readerID)) {
                if (!dao.getAllBorrowedBookByReaderId(readerID).isEmpty()) {
                    return dao.getAllBorrowedBookByReaderId(readerID);
                } else {
                    throw new IllegalArgumentException(UserInterface.READER_DIDNT_BORROW_BOOK);
                }
            } else {
                throw new IllegalArgumentException(UserInterface.READER_NOT_EXIST);
            }
        } else {
            throw new IllegalArgumentException(UserInterface.JUST_NUMBER);
        }
    }

    public void returnBook (String bookId) throws IllegalArgumentException {
        if (bookId.matches("^\\s*\\d+\\s*$")) {
            long bookID = Long.parseLong(bookId);
            if (dao.booksAndReaders.containsKey(bookID)) {
                dao.booksAndReaders.remove(bookID);
            } else {
                throw new IllegalArgumentException(UserInterface.BOOK_NOT_EXIST);
            }
        } else {
            throw new IllegalArgumentException(UserInterface.ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }
    }

    public HashMap<Long, Long> borrowBookToReader(String inputBookIDAndReaderID) throws IllegalArgumentException {
        if (inputBookIDAndReaderID.matches("^\\s*(\\d+)(\\s\\/\\s)(\\d+\\s*)$")) {
            String[] bookIdAndAuthorId = inputBookIDAndReaderID.split("/");
            long bookID = Long.parseLong(bookIdAndAuthorId[0].trim());
            long readerID = Long.parseLong(bookIdAndAuthorId[1].trim());
            if (dao.isExistBookById(bookID) && dao.isExistReaderById(readerID)) {
                dao.booksAndReaders.put(bookID, readerID);
                return dao.booksAndReaders;
            } else {
                throw new IllegalArgumentException(UserInterface.NOT_EXIST);
            }
        } else {
            throw new IllegalArgumentException(UserInterface.ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }
    }


    public void addNewBook(String inputNameAndAuthor) throws IllegalArgumentException {
        if (inputNameAndAuthor.matches("^\\s*\\D([a-zA-Z]+)(\\s\\/\\s)([a-zA-Z]+\\s*)$")) {
            String[] nameAndAuthor = inputNameAndAuthor.split("/");
            dao.books.add(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
        } else {
            throw new IllegalArgumentException(UserInterface.ILLEGAL_ARGUMENT_FOR_ADD_BOOK);
        }
    }

    public void addNewReader(String fullName) throws IllegalArgumentException {
        if (fullName.matches("^[a-zA-Z]+\\s*[a-zA-Z]+$")) {
            dao.readers.add(new Reader(fullName));
        } else {
            throw new IllegalArgumentException(UserInterface.ILLEGAL_ARGUMENT_FOR_ADD_READER);
        }
    }

    public List<Book> printAllBooks () {
        return dao.books;
    }

    public List<Reader> printAllReaders () {
        return dao.readers;
    }
}
