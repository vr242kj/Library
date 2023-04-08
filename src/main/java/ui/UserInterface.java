package ui;

import dao.DAOException;
import entity.Book;
import entity.Reader;
import service.LibraryService;
import service.ServiceException;

import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final LibraryService libraryService = new LibraryService();
    private final Scanner input = new Scanner(System.in);

    public void printMenu() {
        System.out.println("WELCOME TO THE LIBRARY!");

        while (true) {
            System.out.println("""
            
            PLEASE,SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
            \t[1]SHOW ALL BOOKS IN THE LIBRARY
            \t[2]SHOW ALL READERS REGISTERED IN THE LIBRARY
            \t[3]REGISTER NEW READER
            \t[4]ADD NEW BOOK
            \t[5]BORROW A BOOK TO A READER
            \t[6]RETURN A BOOK TO THE LIBRARY
            \t[7]SHOW ALL BORROWED BOOK BY READER ID
            \t[8]SHOW CURRENT READER OF A BOOK WITH ID
                        
            TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!""");

            try {
                switch (input.nextLine().toLowerCase()) {
                    case "1" -> printAllBooks();
                    case "2" -> printAllReaders();
                    case "3" -> addNewReader();
                    case "4" -> addNewBook();
                    case "5" -> borrowBookToReader();
                    case "6" -> returnBookToLibrary();
                    case "7" -> printAllBorrowedBookById();
                    case "8" -> printCurrentReaderOfBookByReaderId();
                    case "exit" -> exitFromProgram();
                    default -> throw new UserInputException("Try again (numbers {1, 8} or EXIT)");
                }
            } catch (ServiceException | DAOException | UserInputException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void printAllBooks () {
        List<Book> books = libraryService.findAllBooks();
        if (books.isEmpty()) {
            System.out.println("There are no books in the library.");
        } else {
            books.forEach(System.out::println);
        }
    }

    private void printAllReaders () {
        List<Book> readers = libraryService.findAllBooks();
        if (readers.isEmpty()) {
            System.out.println("There are no readers in the library.");
        } else {
            readers.forEach(System.out::println);
        }
    }

    private void addNewReader () {
        System.out.println("Please enter new reader full name!");
        String fullName = input.nextLine();
        libraryService.addNewReader(fullName);
        System.out.println("Reader added successfully.");
    }

    private void addNewBook () {
        System.out.println("Please enter new book name and author separated by \"/\". Like this: name/author.");
        String inputNameAndAuthor = input.nextLine();
        libraryService.addNewBook(inputNameAndAuthor);
        System.out.println("Book added successfully.");
    }

    private void borrowBookToReader () {
        System.out.println("Please enter new book id and reader id separated by \"/\". Like this: book id/reader id.");
        String inputBookIDAndAuthorID = input.nextLine();
        libraryService.borrowBookToReader(inputBookIDAndAuthorID);
        System.out.println("Book loaned successfully.");
    }

    private void returnBookToLibrary () {
        System.out.println("Write book id:");
        String bookId = input.nextLine();
        libraryService.returnBook(bookId.trim());
        System.out.println("Book return successfully.");
    }

    private void printAllBorrowedBookById () {
        System.out.println("Write reader id:");
        String readerId = input.nextLine();
        List<Book> listOfBooks = libraryService.allBorrowedBookByReaderId(readerId.trim());

        if (listOfBooks.isEmpty()) {
            System.out.println("This reader hasn't borrowed the book yet.");
        } else {
            listOfBooks.forEach(System.out::println);
        }
    }

    private void printCurrentReaderOfBookByReaderId () {
        System.out.println("Write book id:");
        String bookId = input.nextLine();
        Reader currentReader = libraryService.currentReaderOfBook(bookId.trim());

        if (currentReader == null) {
            System.out.println("This book is in the library.");
        } else {
            System.out.println(currentReader);
        }
    }

    private void exitFromProgram () {
        input.close();
        String EXIT_MESSAGE = "\nGoodbye!";
        System.out.println(EXIT_MESSAGE);
        System.exit(0);
    }
}
