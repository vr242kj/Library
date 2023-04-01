package dao;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class BookDaoImplementation implements DaoBookInterface {
    //String myConnectionURL = "jdbc:postgresql://localhost:5432/library?user=postgres&password=olimp123";
    String URL = "jdbc:postgresql://localhost:5432/library";

    private final List<Book> books = new ArrayList<Book>();
    ConnectionUtil connectionUtil = new ConnectionUtil();
    @Override
    public List<Book> findAll () {
        books.clear();

        String sql = "select * from book";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(createBookFromDB(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }
    @Override
    public Optional<Book> findById (int id) {

        String sql = "select * from book where id = ?";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return Optional.of(createBookFromDB(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAllByReaderId (int readerId) {
        List<Book> books = new ArrayList<Book>();

        String sql = "select * from book where readerId = ?";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(createBookFromDB(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }

    private Book createBookFromDB (ResultSet rs) throws SQLException {
        return new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("readerId"));
    }

    @Override
    public Optional<Book> save (Book bookToSave) {

        String sql = "insert into book(name, author) values(?, ?)";
        int bookToSaveId = 0;

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bookToSave.getName());
            ps.setString(2, bookToSave.getAuthor());
            bookToSaveId = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return findById(bookToSaveId);
    }

    @Override
    public void borrowBookToReader (int bookId, int readerId) {

        String sql = "update book set readerId = ? where id = ?";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void returnBookToLibrary (int bookId) {

        String sql = "update book set readerId = ? where id = ?";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setNull(1, Types.INTEGER);
            ps.setLong(2, bookId);
            ps.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    @Override
    public Integer findReaderIdByBookId (int bookId) {

        String sql = "select readerId from book where id = ?";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return rs.getInt("readerId");
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }
}
