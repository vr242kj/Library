package zd.Service;

import zd.DAO.Dao;
import zd.UI.UserInterface;
import zd.model.Book;
import zd.model.Reader;

public class LibraryService {

    private final Dao dao = new Dao();

    public Reader getReaderById(String readerId) {
        return dao.readers.stream().filter(x -> x.getId() == Integer.parseInt(readerId)).findFirst().get();
    }

    public void currentReaderOfBook (String bookId) {
        if (bookId.matches("^\\s*\\d+\\s*$")) {
            if (dao.isExistBookById(bookId)) {
                dao.bookAndReader.entrySet().stream()
                        .filter(x -> x.getKey().equals(bookId))
                        .forEach(x -> System.out.println(getReaderById(x.getValue())));
            } else {
                UserInterface.printErrorMessage(UserInterface.BOOK_NOT_EXIST);
            }
        } else {
            System.out.println(UserInterface.JUST_NUMBER);
        }
    }

    public Book getBookById (String bookId) {
        return dao.books.stream().filter(x -> x.getId() == Integer.parseInt(bookId)).findFirst().get();
    }

    public void allBorrowedByReaderId (String readerId) {
        if (readerId.matches("^\\s*\\d+\\s*$")) {
            if (dao.isExistReaderById(readerId)) {
                dao.bookAndReader.entrySet().stream()
                        .filter(x -> x.getValue().equals(readerId))
                        .forEach(x -> System.out.println(getBookById(x.getKey()) + " "));
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
        dao.readers.add(new Reader(fullName));
    }

    public void printAllBooks () {
        UserInterface.printAllItems(dao.books);
    }

    public void printAllReaders () {
        UserInterface.printAllItems(dao.readers);
    }
}
