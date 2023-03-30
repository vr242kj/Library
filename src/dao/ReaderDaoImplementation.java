package dao;

import entity.Reader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDaoImplementation implements DaoReaderInterface{

    private final List<Reader> readers = new ArrayList<Reader>();

    ConnectionUtil db = new ConnectionUtil();
    Connection connection = db.connectionToDatabase("library", "postgres", "olimp123");

    Statement statement;
    ResultSet rs;

    @Override
    public List<Reader> findAll() {
        readers.clear();
        try {
            String query = String.format("select * from %s","reader");
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()){
                readers.add(new Reader(rs.getInt("id"), rs.getString("name")));
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return readers;
    }

    @Override
    public Optional<Reader> findById(long id) {
        try {
            String query = String.format("select * from %s where id = %s","reader",id);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                return Optional.of(new Reader(rs.getInt("id"), rs.getString("name")));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return Optional.empty();
    }

    @Override
    public Reader save(Reader readerToSave) {
        try {
            String query = String.format("insert into %s(name) values('%s');", "reader", readerToSave.getName());
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            System.out.println(e);
        }
        return readerToSave;
    }
}
