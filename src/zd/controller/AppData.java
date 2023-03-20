package zd.controller;

import zd.model.Book;
import zd.model.Reader;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AppData {

    private static Scanner input = new Scanner(System.in);

    public static Reader getReaderById(List<Reader> readers, String readerId) {
        for (Reader r : readers) {
            if (r.getId() == Integer.valueOf(readerId)) {
                return r;
            }
        }
        return null;
    }

    public static void currentReaderOfBook(HashMap<String, String> bookAndReader, List<Reader> readers) {
        System.out.println("Write book id:");
        String bookId = input.next();
        String userId = bookAndReader.get(bookId);
        System.out.print(getReaderById(readers, userId).toString());
    }

    public static Book getBookByReaderId(List<Book> books, String bookId) {
        for (Book b : books) {
            if (b.getId() == Integer.valueOf(bookId)) {
                return b;
            }
        }
        return null;
    }

    public static void allBorrowedByReaderId(HashMap<String, String> bookAndReader, List<Book> books) {
        System.out.println("Write reader id:");
        String readerId = input.next();
        for (String key: bookAndReader.keySet())
        {
            if (readerId.equals(bookAndReader.get(key))) {
                System.out.print(getBookByReaderId(books, key).toString() + " ");
            }
        }
    }

    public static void returnBook(HashMap<String, String> bookAndReader) {
        System.out.println("Write book id:");
        String bookId = input.next();
        bookAndReader.remove(bookId.trim());
        System.out.println("New map is: "+ bookAndReader);
    }

    public static void borrowBookToReader(HashMap<String, String> bookAndReader) {
        System.out.println("Please enternew book id and reader id separated by “/”. Like this: book id / reader id");
        String[] nameAndAuthor = input.nextLine().split("/");
        bookAndReader.put(nameAndAuthor[0].trim(), nameAndAuthor[1].trim());
        System.out.println("New map is: "+ bookAndReader);
    }


    public static void addNewBook(List<Book> books) {
        System.out.println("Please enternew book name and author separated by “/”. Like this: name / author");
        String[] nameAndAuthor = input.nextLine().split("/");
        books.add(new Book(nameAndAuthor[0].trim(), nameAndAuthor[1].trim()));
    }

    public static void addNewReader(List<Reader> readers) {
        System.out.println("Please enter new reader full name!");
        String fullName = input.nextLine();
        readers.add(new Reader(fullName));
    }

    public static void printAllBooks(List<Book> books) {
        for(Book book : books) {
            System.out.println(book.toString());
        }
    }

    public static void printAllReaders(List<Reader> readers) {
        for(Reader reader : readers) {
            System.out.println(reader.toString());
        }
    }
}
