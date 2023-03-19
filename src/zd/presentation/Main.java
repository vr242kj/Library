package zd.presentation;

import zd.data.entity.Book;
import zd.data.entity.Reader;

import java.util.Scanner;

public class Main {

    public static final String WELCOME_LINE = "WELCOME TO THE LIBRARY!";
    public static final String MENU = """
                        
            PLEASE,SELECT ONE OF THE FOLLOWING ACTIONS BY TYPING THE OPTION’S NUMBER AND PRESSING ENTER KEY:
            \t[1]SHOW ALL BOOKS IN THE LIBRARY
            \t[2]SHOW ALL READERS REGISTERED IN THE LIBRARY
                        
            TYPE “EXIT” TO STOP THE PROGRAM AND EXIT!""";

    public static final String TRY_AGAIN = "\nIllegal Argument, try again (1, 2 or EXIT)";
    public static final String EXIT_MESSAGE = "\nGoodbye!";

    private static Book book1  = new Book(1, "In Search of Lost Time", "Marcel Proust");
    private static Book book2 = new Book(2, " Ulysses", "James Joyce");
    private static Book book3  = new Book(3, " Don Quixote", "Miguel de Cervantes");

    private static Reader reader1 = new Reader(1, "Gabriel Garcia Marquez");
    private static Reader reader2 = new Reader(2, "F. Scott Fitzgerald");
    private static Reader reader3 = new Reader(3, "Herman Melville");

    public static void main(String[] args) {
        System.out.println(WELCOME_LINE);
        printMenu();
    }

    public static void printMenu() {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println(MENU);

            switch (input.next()) {
                case "1" -> printAllBooks();
                case "2" -> printAllReaders();
                case "EXIT" -> {
                    input.close();
                    System.out.println(EXIT_MESSAGE);
                    System.exit(0);
                }
                default -> System.out.println(TRY_AGAIN);
            }
        }
    }

    public static void printAllBooks(){
        System.out.println(book1.toString());
        System.out.println(book2.toString());
        System.out.println(book3.toString());
    }

    public static void printAllReaders(){
        System.out.println(reader1.toString());
        System.out.println(reader2.toString());
        System.out.println(reader3.toString());
    }
}
