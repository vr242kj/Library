package zd.view;

import zd.model.Book;
import zd.model.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static zd.controller.AppData.*;

public class Main {

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
    public static final String EXIT_MESSAGE = "\nGoodbye!";

    public static void main(String[] args) {
        List<Book> books = new ArrayList<Book>();
        List<Reader> readers = new ArrayList<Reader>();
        HashMap<String, String> bookAndReader = new HashMap<String, String>();

        books.add(new Book("In Search of Lost Time", "Marcel Proust"));
        books.add(new Book( " Ulysses", "James Joyce"));
        books.add(new Book( " Don Quixote", "Miguel de Cervantes"));

        readers.add(new Reader( "Gabriel Garcia Marquez"));
        readers.add(new Reader( "F. Scott Fitzgerald"));
        readers.add(new Reader( "Herman Melville"));

        System.out.println(WELCOME_LINE);
        printMenu(books, readers, bookAndReader);
    }

    public static void printMenu(List<Book> books, List<Reader> readers, HashMap<String, String> bookAndReader) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println(MENU);

            switch (input.next()) {
                case "1" -> printAllBooks(books);
                case "2" -> printAllReaders(readers);
                case "3" -> addNewReader(readers);
                case "4" -> addNewBook(books);
                case "5" -> borrowBookToReader(bookAndReader);
                case "6" -> returnBook(bookAndReader);
                case "7" -> allBorrowedByReaderId(bookAndReader, books);
                case "8" -> currentReaderOfBook(bookAndReader, readers);
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
