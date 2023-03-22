package zd.UI;

import zd.Service.LibraryService;
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

    public static final String TRY_AGAIN = "\nIllegal Argument, try again (1, 2 or EXIT)";
    public static final String JUST_NUMBER = "\nIllegal Argument, try again (just number)";
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
                    if (inputNameAndAuthor.matches("^\\s*\\D([a-zA-Z]+)(\\s\\/\\s)([a-zA-Z]+\\s*)$")) {
                        String[] nameAndAuthor = inputNameAndAuthor.split("/");
                        libraryService.addNewBook(nameAndAuthor);
                    } else {
                        System.out.println("Illegal Argument, try again like this: name / author");
                   }
                }
                case "5" -> {
                    System.out.println("Please enter new book id and reader id separated by “/”. Like this: book id / reader id");
                    String inputBookIDAndAuthorID = input.nextLine();
                    if (inputBookIDAndAuthorID.matches("^\\s*(\\d+)(\\s\\/\\s)(\\d+\\s*)$")) {
                        String[] bookIdAndAuthorId = inputBookIDAndAuthorID.split("/");
                        libraryService.borrowBookToReader(bookIdAndAuthorId);
                        System.out.println("New map is: ");
                        libraryService.printBookAndReader();
                    } else {
                        System.out.println("Illegal Argument, try again like this: book id / reader id");
                    }
                }
                case "6" -> {
                    System.out.println("Write book id:");
                    String bookId = input.nextLine();
                    if (bookId.matches("^\\s*\\d+\\s*$")) {
                        libraryService.returnBook(bookId.trim());
                    } else {
                        System.out.println(JUST_NUMBER);
                    }
                }

                case "7" -> {
                    System.out.println("Write reader id:");
                    String readerId = input.nextLine();
                    if (readerId.matches("^\\s*\\d+\\s*$")) {
                        libraryService.allBorrowedByReaderId(readerId.trim());
                    } else {
                        System.out.println(JUST_NUMBER);
                    }
                }
                case "8" -> {
                    System.out.println("Write book id:");
                    String bookId = input.nextLine();
                    if (bookId.matches("^\\s*\\d+\\s*$")) {
                        libraryService.currentReaderOfBook(bookId.trim());
                    } else {
                        System.out.println(JUST_NUMBER);
                    }
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
}
