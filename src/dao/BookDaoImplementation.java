package dao;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImplementation implements DaoBookInterface {

    private final List<Book> books = new ArrayList<Book>();

    ConnectionUtil db = new ConnectionUtil();
    Connection connection = db.connectionToDatabase("library", "postgres", "olimp123");
    Statement statement;
    ResultSet rs;

    @Override
    public List<Book> findAll() {
        books.clear();
        try {
            String query = String.format("select * from %s", "book");
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()){
                books.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("readerId")));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return books;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            String query=String.format("select * from %s where id = %s", "book", id);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                return Optional.of(new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("readerId")));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAllByReaderId (long readerId) {
        List<Book> books = new ArrayList<Book>();
        try {
            String query = String.format("select * from %s where readerId = %s", "book", readerId);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()){
                books.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("readerId")));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return books;
    }

    @Override
    public Book save (Book bookToSave) {
        try {
            String query = String.format("insert into %s(name,author) values('%s','%s');", "book", bookToSave.getName(), bookToSave.getAuthor());
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            System.out.println(e);
        }
        return bookToSave;
    }

    @Override
    public void borrowBookToReader (long bookId, long readerId) {
        try {
            String query = String.format("update %s set readerId='%s' where id='%s'", "book", readerId, bookId);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Update");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void returnBookToLibrary(long bookId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update book set readerId = ? where id = ?");
            preparedStatement.setNull(1, Types.INTEGER);
            preparedStatement.setLong(2, bookId);
            preparedStatement.executeUpdate();
            System.out.println("Row Update");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public Long findReaderIdByBookId(long bookId) {
        try {
            String query = String.format("select readerId from %s where id = %s", "book", bookId);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
               return Long.valueOf(rs.getInt("readerId"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return 0L;
    }
}
