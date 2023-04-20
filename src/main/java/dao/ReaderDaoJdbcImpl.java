package dao;

import entity.Book;
import entity.Reader;

import java.sql.*;
import java.util.*;
import java.util.stream.Stream;

public class ReaderDaoJdbcImpl implements ReaderDao {
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
                if (resultSet.next()) {
                    return Optional.of(mapToReader(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval reader id!" + "\nError details: " + e.getMessage());
        }
    }

    private Reader mapToReader(ResultSet rs) {
        try {
            return new Reader(
                    rs.getInt("id"),
                    rs.getString("name")
            );
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
                if (resultSet.next()) {
                    return Optional.of(mapToReader(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval reader by book Id!" + "\nError details: " + e.getMessage());
        }
    }

    public Map<Reader, List<Book>> findAllWithBooks() {
        var query = """
                select
                    reader.id,
                    reader.name,
                    book.id as bookId,
                    book.name as bookName,
                    book.author as bookAuthor
                from reader
                    inner join book on reader.id = book.readerid
                """;
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement(query)) {
            Map<Reader, List<Book>> map = new TreeMap<>(Comparator.comparing(Reader::getId));
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var book = new Book(
                            resultSet.getLong("bookId"),
                            resultSet.getString("bookName"),
                            resultSet.getString("bookAuthor")
                    );
                    map.merge(
                            mapToReader(resultSet),
                            List.of(book),
                            (addedBooks, newBooks) -> Stream
                                .concat(addedBooks.stream(), newBooks.stream())
                                .toList()
                    );
                }
            }
            return map;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available readers with borrowed books list!" + "\nError details: " + e.getMessage());
        }
    }
}