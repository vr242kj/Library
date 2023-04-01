package ui;

import entity.Book;
import entity.Reader;
import service.LibraryService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserInterface {
    private final String MENU = """
                        
            PLEASE,SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
            \t[1]SHOW ALL BOOKS IN THE LIBRARY
            \t[2]SHOW ALL READERS REGISTERED IN THE LIBRARY
            \t[3]REGISTER NEW READER
            \t[4]ADD NEW BOOK
            \t[5]BORROW A BOOK TO A READER
            \t[6]RETURN A BOOK TO THE LIBRARY
            \t[7]SHOW ALL BORROWED BOOK BY USER ID
            \t[8]SHOW CURRENT READER OF A BOOK WITH ID
                        
            TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!""";

    private final LibraryService libraryService = new LibraryService();
    private final Scanner input = new Scanner(System.in);

    public void printMenu() {
        String WELCOME_LINE = "WELCOME TO THE LIBRARY!";
        System.out.println(WELCOME_LINE);

        while (true) {
            System.out.println(MENU);
            try {
                String TRY_AGAIN = "\nTry again (numbers {1, 8} or EXIT)";
                switch (input.nextLine()) {
                    case "1" -> printAllBooks();
                    case "2" -> printAllReaders();
                    case "3" -> addNewReader();
                    case "4" -> addNewBook();
                    case "5" -> borrowBookToReader();
                    case "6" -> returnBookToLibrary();
                    case "7" -> printAllBorrowedBookById();
                    case "8" -> printCurrentReaderOfBookByReaderId();
                    case "EXIT" -> exitFromProgram();
                    default -> throw new IllegalArgumentException(TRY_AGAIN);
                }
            } catch (IllegalArgumentException e){
                System.err.println(e);
            }
        }
    }

    private void printAllBooks () {
        libraryService.findAllBooks().forEach(System.out::println);
    }

    private void printAllReaders () {
        libraryService.findAllReaders().forEach(System.out::println);
    }

    private void addNewReader () {
        System.out.println("Please enter new reader full name!");
        String fullName = input.nextLine();
        libraryService.addNewReader(fullName);
    }

    private void addNewBook () {
        System.out.println("Please enter new book name and author separated by \"/\". Like this: name / author");
        String inputNameAndAuthor = input.nextLine();
        libraryService.addNewBook(inputNameAndAuthor);
    }

    private void borrowBookToReader () {
        System.out.println("Please enter new book id and reader id separated by \"/\". Like this: book id / reader id");
        String inputBookIDAndAuthorID = input.nextLine();
        libraryService.borrowBookToReader(inputBookIDAndAuthorID);
    }

    private void returnBookToLibrary () {
        System.out.println("Write book id:");
        String bookId = input.nextLine();
        libraryService.returnBook(bookId.trim());
        System.out.println("Book return successful");
    }

    private void printAllBorrowedBookById () {
        System.out.println("Write reader id:");
        String readerId = input.nextLine();
        List<Book> listOfBooks = libraryService.allBorrowedBookByReaderId(readerId.trim());

        if (listOfBooks.isEmpty()) {
            System.out.println("This reader hasn't borrowed the book yet");
        } else {
            listOfBooks.forEach(System.out::println);
        }
    }

    private void printCurrentReaderOfBookByReaderId () {
        System.out.println("Write book id:");
        String bookId = input.nextLine();
        Optional<Reader> currentReader = libraryService.currentReaderOfBook(bookId.trim());

        if (currentReader.isEmpty()) {
            System.out.println("This book is in the library");
        } else {
            System.out.println(currentReader.get());
        }
    }

    private void exitFromProgram () {
        input.close();
        String EXIT_MESSAGE = "\nGoodbye!";
        System.out.println(EXIT_MESSAGE);
        System.exit(0);
    }
}
