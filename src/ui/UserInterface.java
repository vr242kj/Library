package ui;

import service.LibraryService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserInterface {
    private static final String WELCOME_LINE = "WELCOME TO THE LIBRARY!";
    private static final String MENU = """
                        
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

    private static final String TRY_AGAIN = "\nIllegal Argument, try again (numbers {1, 8} or EXIT)";
    private static final String EXIT_MESSAGE = "\nGoodbye!";

    private final String BOOK_IN_LIBRARY = "This book is in the library";

    private final LibraryService libraryService = new LibraryService();
    private final Scanner input = new Scanner(System.in);

    public void printMenu() {
        System.out.println(WELCOME_LINE);

        while (true) {
            System.out.println(MENU);
            try {
                switch (input.nextLine()) {
                    case "1" -> printAllBooks();
                    case "2" -> printAllReaders();
                    case "3" -> addNewReader();
                    case "4" -> addNewBook();
                    case "5" -> borrowBookToReader();
                    case "6" -> returnBookToLibrary();
                    case "7" -> showAllBorrowedBookById();
                    case "8" -> showCurrentReaderOfBookByReaderId();
                    case "EXIT" -> exitFromProgram();
                    default -> throw new IllegalArgumentException(UserInterface.TRY_AGAIN);
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

        if (libraryService.returnBook(bookId.trim()) == null) {
            System.out.println(BOOK_IN_LIBRARY);
        } else {
            System.out.println("Book return successful");
        }
    }

    private void showAllBorrowedBookById () {
        System.out.println("Write reader id:");
        String readerId = input.nextLine();
        printAllItemsInCollection(libraryService.allBorrowedBookByReaderId(readerId.trim()));
    }

    private void showCurrentReaderOfBookByReaderId () {
        System.out.println("Write book id:");
        String bookId = input.nextLine();
        if (libraryService.currentReaderOfBook(bookId.trim()).isEmpty()) {
            System.out.println(BOOK_IN_LIBRARY);
        } else {
            printItemInCollection(libraryService.currentReaderOfBook(bookId.trim()));
        }
    }

    private void exitFromProgram () {
        input.close();
        System.out.println(EXIT_MESSAGE);
        System.exit(0);
    }

    private <T> void printAllItemsInCollection (List<T> data) {
        data.forEach(System.out::println);
    }

    public <T> void printItemInCollection (Optional<T> item) {
        System.out.println(item.get());
    }
}
