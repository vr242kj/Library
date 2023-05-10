package com.example.ui;

import com.example.dao.BookDaoJdbcImpl;
import com.example.dao.DAOException;
import com.example.dao.ReaderDaoJdbcImpl;

import com.example.entity.Book;
import com.example.entity.Reader;
import com.example.service.LibraryService;
import com.example.service.ServiceException;

import java.util.*;

public class UserInterface {
    private final LibraryService libraryService = new LibraryService(new BookDaoJdbcImpl(), new ReaderDaoJdbcImpl());

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
            \t[9]SHOW ALL READERS WITH THEIR BORROWED BOOKS
            \t[10]SHOW ALL BOOKS WITH READERS            
                        
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
                    case "8" -> printCurrentReaderOfBookByBookId();
                    case "9" -> printAllReadersWithBorrowedBooks();
                    case "10" -> printAllBooksWithReaders();
                    case "exit" -> exitFromProgram();
                    default -> throw new UserInputException("Try again (numbers {1, 10} or EXIT)");
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
        List<Reader> readers = libraryService.findAllReaders();
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
        System.out.println("Book borrowed successfully.");
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

    private void printCurrentReaderOfBookByBookId () {
        System.out.println("Write book id:");
        String bookId = input.nextLine();

        libraryService.currentReaderOfBook(bookId.trim())
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("This book is in the library.")
                );
    }
    private void printAllReadersWithBorrowedBooks() {
        Map<Reader, List<Book>> allReadersWithBorrowedBooks = libraryService.findAllReadersWithBorrowedBooks();
        if (allReadersWithBorrowedBooks.isEmpty()) {
            System.out.println("There are no books borrowed.");
        } else {
            allReadersWithBorrowedBooks.forEach((key, value) -> System.out.println(key + " : " + value));
        }
    }

    private void printAllBooksWithReaders() {
        libraryService.findAllBooksWithReaders().forEach(
                (key, value) ->
                        value.ifPresentOrElse(
                                reader -> System.out.println(key + " : " + reader),
                                () -> System.out.println(key + " : available")
                        )
        );
    }

    private void exitFromProgram () {
        input.close();
        String EXIT_MESSAGE = "\nGoodbye!";
        System.out.println(EXIT_MESSAGE);
        System.exit(0);
    }
}