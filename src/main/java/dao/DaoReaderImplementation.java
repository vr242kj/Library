package dao;

import entity.Book;
import entity.Reader;

import java.sql.*;
import java.util.*;

public class DaoReaderImplementation implements DaoReaderInterface {
    @Override
    public List<Reader> findAll () {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from reader")) {
            List<Reader> readers = new ArrayList<Reader>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    readers.add(mapToReader(resultSet));
                }
            }
            return readers;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available readers list!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Optional<Reader> findById (long id) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from reader where id = ?")) {
            statement.setLong(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return Optional.of(mapToReader(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval reader id!" + "\nError details: " + e.getMessage());
        }
    }

    private Reader mapToReader (ResultSet rs) {
        try {
            return new Reader(rs.getInt("id"), rs.getString("name"));
        } catch (SQLException e) {
            throw new DAOException("Failed to map resultSet to Reader object!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Reader save (Reader readerToSave) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("insert into reader(name) values(?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, readerToSave.getName());
            statement.executeUpdate();
            try(var generatedKeys =  statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    readerToSave.setId(generatedKeys.getInt(1));
                    return readerToSave;
                } else {
                    throw new DAOException("Creating reader failed, no ID obtained!");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Database error during saving new reader!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Optional<Reader> findByBookId(long bookId) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select reader.id, reader.name from reader inner join book on reader.id = book.readerid where book.id = ?")) {
            statement.setLong(1, bookId);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return Optional.of(mapToReader(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval reader by book Id!" + "\nError details: " + e.getMessage());
        }
    }
    public Map<Reader, Optional<Book>> findAllWithBooks () {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select reader.id, reader.name, book.id from reader inner join book on reader.id = book.readerid order by reader.id")) {
            Map<Reader, Optional<Book>> map = new HashMap<>();
            DaoBookImplementation daoBookImplementation = new DaoBookImplementation();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    map.put(mapToReader(resultSet), daoBookImplementation.findById(resultSet.getLong(3)));
                }
            }
            return map;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available readers with borrowed books list!" + "\nError details: " + e.getMessage());
        }
    }

}
