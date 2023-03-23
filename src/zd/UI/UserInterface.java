package zd.UI;

import zd.Service.LibraryService;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    public static final String WELCOME_LINE = "WELCOME TO THE LIBRARY!";
    public static final String MENU = """
                        
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

    public static final String TRY_AGAIN = "\nIllegal Argument, try again (numbers {1, 8} or EXIT)";
    public static final String JUST_NUMBER = "\nIllegal Argument, try again (just number)";
    public static final String BOOK_NOT_EXIST = "This book id doesn't exist";
    public static final String READER_NOT_EXIST = "This reader id doesn't exist";
    public static final String NOT_EXIST = "Book id or reader id doesn't exist";
    public static final String BOOK_IN_LIBRARY = "This book is in the library";
    public static final String READER_DIDNT_BORROW_BOOK = "This reader hasn't borrowed the book yet";
    public static final String ILLEGAL_ARGUMENT_FOR_RETURN_BOOK = "Illegal Argument, try again only number";
    public static final String ILLEGAL_ARGUMENT_FOR_ADD_BOOK = "Illegal Argument, try again like this: name / author";
    public static final String ILLEGAL_ARGUMENT_FOR_ADD_READER = "Illegal Argument, try again only letters";
    public static final String EXIT_MESSAGE = "\nGoodbye!";

    public static void printMenu() {
        System.out.println(WELCOME_LINE);

        LibraryService libraryService = new LibraryService();
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println(MENU);

            switch (input.nextLine()) {
                case "1" -> libraryService.printAllBooks();
                case "2" -> libraryService.printAllReaders();
                case "3" -> {
                    System.out.println("Please enter new reader full name!");
                    String fullName = input.nextLine();
                    libraryService.addNewReader(fullName);
                }
                case "4" -> {
                    System.out.println("Please enter new book name and author separated by \"/\". Like this: name / author");
                    String inputNameAndAuthor = input.nextLine();
                    libraryService.addNewBook(inputNameAndAuthor);
                }
                case "5" -> {
                    System.out.println("Please enter new book id and reader id separated by \"/\". Like this: book id / reader id");
                    String inputBookIDAndAuthorID = input.nextLine();
                    libraryService.borrowBookToReader(inputBookIDAndAuthorID);
                }
                case "6" -> {
                    System.out.println("Write book id:");
                    String bookId = input.nextLine();
                    libraryService.returnBook(bookId.trim());
                }

                case "7" -> {
                    System.out.println("Write reader id:");
                    String readerId = input.nextLine();
                    libraryService.allBorrowedBookByReaderId(readerId.trim());
                }
                case "8" -> {
                    System.out.println("Write book id:");
                    String bookId = input.nextLine();
                    libraryService.currentReaderOfBook(bookId.trim());
                }
                case "EXIT" -> {
                    input.close();
                    System.out.println(EXIT_MESSAGE);
                    System.exit(0);
                }
                default -> System.out.println(TRY_AGAIN);
            }
        }
    }

    public static void printErrorMessage (String message) {
        System.out.println(message);
    }

    public static <T> void printAllItemsInCollection (List<T> data) {
        data.forEach(System.out::println);
    }

    public static <T> void printItemInCollection (T item) {
        System.out.println(item);
    }

    public static void printMapBookToReader(HashMap<String, String> bookAndReader) {
        System.out.println("New map is: ");
        bookAndReader.entrySet()
                .forEach(System.out::println);
    }
}
