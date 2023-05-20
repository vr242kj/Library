package com.example.dao;

import com.example.entity.Book;
import com.example.entity.Reader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class ReaderDaoJdbcTemplateImpl implements ReaderDao {

    private final JdbcTemplate jdbcTemplate;

    public ReaderDaoJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Reader> findAll() {
        return jdbcTemplate.query("select * from reader",
                this::mapToReader);
    }

    @Override
    public Optional<Reader> findById(long id) {
        return jdbcTemplate.query("select * from reader where id = ?",
                        this::mapToReader, id)
                .stream().findFirst();
    }

    @Override
    public Reader save(Reader readerToSave) {
        String SQL_INSERT = "insert into reader(name) values(?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, readerToSave.getName());
            return ps;
        }, keyHolder);

        var generatedId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        readerToSave.setId(generatedId);

        return readerToSave;
    }

    @Override
    public Optional<Reader> findByBookId(long bookId) {
        return jdbcTemplate.query("select reader.id, reader.name from reader inner join book on reader.id = book.readerid where book.id = ?",
                this::mapToReader, bookId)
                .stream().findFirst();
    }

    @Override
    public Map<Reader, List<Book>> findAllWithBooks() {
        var FIND_ALL_READERS_WITH_BOOK = """
                select
                    reader.id,
                    reader.name,
                    book.id as bookId,
                    book.name as bookName,
                    book.author as bookAuthor
                from reader
                    inner join book on reader.id = book.readerid
                """;

        Map<Reader, List<Book>> readersWithBook = new TreeMap<>(Comparator.comparing(Reader::getId));

        jdbcTemplate.query(FIND_ALL_READERS_WITH_BOOK, (rs, rowNum) -> {
            var book = new Book(
                    rs.getLong("bookId"),
                    rs.getString("bookName"),
                    rs.getString("bookAuthor")
            );

            readersWithBook.merge(
                    mapToReader(rs, rowNum),
                    List.of(book),
                    (addedBooks, newBooks) -> Stream
                            .concat(addedBooks.stream(), newBooks.stream())
                            .toList()
            );

            return book;
        });

        return readersWithBook;
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from reader where id = ?", id);
    }

    private Reader mapToReader(ResultSet rs, int rowNum) {
        try {
            return new Reader(
                    rs.getInt("id"),
                    rs.getString("name")
            );
        } catch (SQLException e) {
            throw new DAOException("Failed to map resultSet to Reader object!" + "\nError details: " + e.getMessage());
        }
    }
}
