package dao;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoBookImplementation implements DaoBookInterface {
    @Override
    public List<Book> findAll () {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from book")) {
            List<Book> books = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapToBook(resultSet));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available books list!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Optional<Book> findById (long id) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from book where id = ?")) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return Optional.of(mapToBook(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval book id!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public List<Book> findAllByReaderId (long readerId) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from book where readerId = ?")) {
            statement.setLong(1, readerId);
            List<Book> books = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapToBook(resultSet));
                }
            }
            return books;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available books by reader id!" + "\nError details: " + e.getMessage());
        }
    }

    private Book mapToBook(ResultSet rs) {
        try {
            return new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("readerId"));
        } catch (SQLException e) {
            throw new DAOException("Failed to map resultSet to Book object!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Book save (Book bookToSave) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("insert into book(name, author) values(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bookToSave.getName());
            statement.setString(2, bookToSave.getAuthor());
            statement.executeUpdate();
            try(var generatedKeys =  statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                        bookToSave.setId(generatedKeys.getInt(1));
                        return bookToSave;
                } else {
                    throw new DAOException("Creating book failed, no ID obtained!");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error during saving new book!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public void borrowBookToReader (long bookId, long readerId) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("update book set readerId = ? where id = ?")) {
            statement.setLong(1, readerId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Database error, statement cannot be executed!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public void returnBookToLibrary (long bookId) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("update book set readerId = null where id = ?")) {
            statement.setLong(1, bookId);
            statement.executeUpdate();
        }catch (SQLException e){
            throw new DAOException("Database error, statement cannot be executed!" + "\nError details: " + e.getMessage());
        }
    }
}
