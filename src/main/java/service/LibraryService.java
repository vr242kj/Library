package service;

import dao.DaoBookImplementation;
import dao.DaoBookInterface;
import dao.DaoReaderInterface;
import dao.DaoReaderImplementation;
import entity.Book;
import entity.Reader;

import java.util.List;

public class LibraryService {
    private final DaoBookInterface daoBookImplementation = new DaoBookImplementation();
    private final DaoReaderInterface daoReaderImplementation = new DaoReaderImplementation();

   public Reader currentReaderOfBook (String bookId) {
       int bookID;

       try{
           bookID = Integer.parseInt(bookId);
       }catch (NumberFormatException e) {
           throw new ServiceException("Book id must be numeric");
       }

       daoBookImplementation.findById(bookID)
               .orElseThrow(() -> new ServiceException("This book id doesn't exist"));

        Integer readerID = daoBookImplementation.findReaderIdByBookId(bookID);

        return daoReaderImplementation.findById(readerID).orElse(null);
    }

    public List<Book> allBorrowedBookByReaderId (String readerId) {
        int readerID;

        try{
            readerID = Integer.parseInt(readerId);
        }catch (NumberFormatException e) {
            throw new ServiceException("Reader id must be numeric");
        }

        daoReaderImplementation.findById(readerID)
                .orElseThrow(() -> new ServiceException("This reader id doesn't exist"));

        return daoBookImplementation.findAllByReaderId(readerID);
    }

    public void returnBook (String bookId) {
        int bookID;

        try{
            bookID = Integer.parseInt(bookId);
        }catch (NumberFormatException e) {
            throw new ServiceException("Book id must be numeric");
        }

        daoBookImplementation.findById(bookID)
                .orElseThrow(() -> new ServiceException("This book id doesn't exist"));

        daoBookImplementation.returnBookToLibrary(bookID);
    }

    public void borrowBookToReader (String inputBookIDAndReaderID) {
        if (!inputBookIDAndReaderID.matches("^\\s*\\d+\\/(\\d+\\s*)$")) {
            throw new ServiceException("Try again like this: book id/reader id. One slash, without spaces before and after. " +
                    "Book id and reader id must be numeric");
        }

        String[] bookIdAndAuthorId = inputBookIDAndReaderID.split("/");

        int bookID = Integer.parseInt(bookIdAndAuthorId[0].trim());
        int readerID = Integer.parseInt(bookIdAndAuthorId[1].trim());

        if (daoBookImplementation.findById(bookID).isEmpty()) {
            throw new ServiceException("This book id doesn't exist");
        }

        if (daoReaderImplementation.findById(readerID).isEmpty()) {
            throw new ServiceException("This reader id doesn't exist");
        }

        daoBookImplementation.borrowBookToReader(bookID, readerID);
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
}
