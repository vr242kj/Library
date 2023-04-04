package dao;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImplementation implements DaoBookInterface {
    @Override
    public List<Book> findAll () {

        String sql = "select * from book";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql)) {
            List<Book> books = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(bookMapper(resultSet));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available books list!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Optional<Book> findById (int id) {

        String sql = "select * from book where id = ?";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return Optional.of(bookMapper(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval book id!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public List<Book> findAllByReaderId (int readerId) {
        List<Book> books = new ArrayList<>();

        String sql = "select * from book where readerId = ?";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, readerId);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(bookMapper(resultSet));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available books by reader id!" + "\nError details: " + e.getMessage());
        }
    }

    private Book bookMapper (ResultSet rs) throws SQLException {
        return new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("readerId"));
    }

    @Override
    public Book save (Book bookToSave) {

        String sql = "insert into book(name, author) values(?, ?)";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bookToSave.getName());
            statement.setString(2, bookToSave.getAuthor());
            statement.executeUpdate();
            try(var generatedKeys =  statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bookToSave.setId(generatedKeys.getInt(1));
                }
            }
            return bookToSave;
        } catch (SQLException e) {
            throw new DAOException("Database error during saving new book!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public void borrowBookToReader (int bookId, int readerId) {

        String sql = "update book set readerId = ? where id = ";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, readerId);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database error, statement cannot be executed!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public void returnBookToLibrary (int bookId) {

        String sql = "update book set readerId = ? where id = ?";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setNull(1, Types.INTEGER);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new DAOException("Database error, statement cannot be executed!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Integer findReaderIdByBookId (int bookId) {

        String sql = "select readerId from book where id = ?";

        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getInt("readerId");
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval reader by book Id!" + "\nError details: " + e.getMessage());
        }
    }
}
