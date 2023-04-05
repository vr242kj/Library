package dao;

import entity.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoReaderImplementation implements DaoReaderInterface {
    @Override
    public List<Reader> findAll () {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from reader")) {
            List<Reader> readers = new ArrayList<Reader>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    readers.add(readerMapper(resultSet));
                }
            }
            return readers;
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval of available readers list!" + "\nError details: " + e.getMessage());
        }
    }

    @Override
    public Optional<Reader> findById (int id) {
        try (var connection = ConnectionUtil.createConnection();
             var statement = connection.prepareStatement("select * from reader where id = ?")) {
            statement.setInt(1, id);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    return Optional.of(readerMapper(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Database error during retrieval reader id!" + "\nError details: " + e.getMessage());
        }
    }

    private Reader readerMapper (ResultSet rs) throws SQLException {
        return new Reader(rs.getInt("id"), rs.getString("name"));
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
}
