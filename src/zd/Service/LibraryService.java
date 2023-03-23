package zd.Service;

import zd.DAO.Dao;
import zd.UI.UserInterface;

import zd.model.Book;
import zd.model.Reader;

public class LibraryService {

    private final Dao dao = new Dao();

    public void currentReaderOfBook (String bookId) {
        if (bookId.matches("^\\s*\\d+\\s*$")) {
            if (dao.isExistBookById(bookId)) {
                if (dao.getCurrentReaderOfBook(bookId) != null) {
                    UserInterface.printItemInCollection(dao.getCurrentReaderOfBook(bookId));
                } else {
                    UserInterface.printErrorMessage(UserInterface.BOOK_IN_LIBRARY);
                }
            } else {
                UserInterface.printErrorMessage(UserInterface.BOOK_NOT_EXIST);
            }
        } else {
            System.out.println(UserInterface.JUST_NUMBER);
        }
    }

    public void allBorrowedBookByReaderId (String readerId) {
        if (readerId.matches("^\\s*\\d+\\s*$")) {
            if (dao.isExistReaderById(readerId)) {
                if (dao.getAllBorrowedBookByReaderId(readerId) != null) {
                    UserInterface.printAllItemsInCollection(dao.getAllBorrowedBookByReaderId(readerId));;
                } else {
                    UserInterface.printErrorMessage(UserInterface.READER_DIDNT_BORROW_BOOK);
                }
            } else {
                UserInterface.printErrorMessage(UserInterface.READER_NOT_EXIST);
            }
        } else {
            UserInterface.printErrorMessage(UserInterface.JUST_NUMBER);
        }
    }

    public void returnBook (String bookId) {
        if (bookId.matches("^\\s*\\d+\\s*$")) {
            if (dao.bookAndReader.containsKey(bookId)) {
                dao.bookAndReader.remove(bookId);
            } else {
                UserInterface.printErrorMessage(UserInterface.BOOK_NOT_EXIST);
            }
        } else {
            UserInterface.printErrorMessage(UserInterface.ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }
    }

    public void borrowBookToReader(String inputBookIDAndAuthorID) {
        if (inputBookIDAndAuthorID.matches("^\\s*(\\d+)(\\s\\/\\s)(\\d+\\s*)$")) {
            String[] bookIdAndAuthorId = inputBookIDAndAuthorID.split("/");
            if (dao.isExistBookById(bookIdAndAuthorId[0].trim()) && dao.isExistReaderById(bookIdAndAuthorId[1].trim())) {
                dao.bookAndReader.put(bookIdAndAuthorId[0].trim(), bookIdAndAuthorId[1].trim());
                UserInterface.printMapBookToReader(dao.bookAndReader);
            } else {
                UserInterface.printErrorMessage(UserInterface.NOT_EXIST);
            }
        } else {
            UserInterface.printErrorMessage(UserInterface.ILLEGAL_ARGUMENT_FOR_RETURN_BOOK);
        }
    }


    public void addNewBook (String inputNameAndAuthor) {
        if (inputNameAndAuthor.matches("^\\s*\\D([a-zA-Z]+)(\\s\\/\\s)([a-zA-Z]+\\s*)$")) {
            String[] nameAndAuthor = inputNameAndAuthor.split("/");
            dao.books.add(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
        } else {
            UserInterface.printErrorMessage(UserInterface.ILLEGAL_ARGUMENT_FOR_ADD_BOOK);
        }
    }

    public void addNewReader (String fullName) {
        if (fullName.matches("^[a-zA-Z]+\\s*[a-zA-Z]+$")) {
            dao.readers.add(new Reader(fullName));
        } else {
            UserInterface.printErrorMessage(UserInterface.ILLEGAL_ARGUMENT_FOR_ADD_READER);
        }
    }

    public void printAllBooks () {
        UserInterface.printAllItemsInCollection(dao.books);
    }

    public void printAllReaders () {
        UserInterface.printAllItemsInCollection(dao.readers);
    }
}
