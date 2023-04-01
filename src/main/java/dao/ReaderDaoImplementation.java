package dao;

import entity.Reader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ReaderDaoImplementation implements DaoReaderInterface{
    //String myConnectionURL = "jdbc:postgresql://localhost:5432/library?user=postgres&password=olimp123";
    String URL = "jdbc:postgresql://localhost:5432/library";
    ConnectionUtil connectionUtil = new ConnectionUtil();
    private final List<Reader> readers = new ArrayList<Reader>();
    @Override
    public List<Reader> findAll() {
        readers.clear();

        String sql = "select * from reader";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    readers.add(createReaderFromBD(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return readers;
    }

    @Override
    public Optional<Reader> findById(int id) {

        String sql = "select * from reader where id = ?";

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    return Optional.of(createReaderFromBD(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return Optional.empty();
    }

    private Reader createReaderFromBD (ResultSet rs) throws SQLException {
        return new Reader(rs.getInt("id"), rs.getString("name"));
    }

    @Override
    public Optional<Reader> save(Reader readerToSave) {

        String sql = "insert into reader(name) values(?)";
        int readerToSaveId = 0;

        try (Connection con = DriverManager.getConnection(URL, connectionUtil.propertiesForConnection());
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, readerToSave.getName());
            readerToSaveId = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return findById(readerToSaveId);
    }
}
