package com.example.dao;

import com.example.entity.Book;
import com.example.entity.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    private static final Logger logger = LoggerFactory.getLogger(BookDaoJdbcTemplateImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Reader> findAll() {
        try {
            return jdbcTemplate.query("select * from reader",
                    this::mapToReader);
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieval reader from the database, due to DB internal error: {}", ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve reader from the database.", ex);
        }
    }

    @Override
    public Optional<Reader> findById(long id) {
        try {
            return jdbcTemplate.query("select * from reader where id = ?",
                            this::mapToReader, id)
                    .stream().findFirst();
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve reader with id {} from the database, due to DB internal error: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve reader from the database.", ex);
        }
    }

    @Override
    public Reader save(Reader readerToSave) {
        String SQL_INSERT = "insert into reader(name) values(?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, readerToSave.getName());
                return ps;
            }, keyHolder);
        } catch (DataAccessException ex) {
            logger.error("Failed to create new book {} in DB, due to DB internal error: {}", readerToSave, ex.getLocalizedMessage());
            throw new DAOException("Failed to save new book: " + readerToSave.toString(), ex);
        }

        Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Integer.class::cast)
                .ifPresentOrElse(
                        readerToSave::setId,
                        () -> {
                            logger.error("Generated ID is null for reader: {}", readerToSave);
                            throw new DAOException("Failed to retrieve generated ID for reader: " + readerToSave.toString());
                        }
                );

        return readerToSave;
    }

    @Override
    public Optional<Reader> findByBookId(long bookId) {
        String SQL_SELECT = """
                select
                    reader.id,
                    reader.name 
                from reader 
                    inner join book on reader.id = book.readerid 
                where book.id = ?
                """;

        try {
            return jdbcTemplate.query(SQL_SELECT, this::mapToReader, bookId).stream()
                    .findFirst();
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve reader by bookId: {}. Error details: {}", bookId, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve reader by bookId: " + bookId, ex);
        }
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

        try {
            jdbcTemplate.query(FIND_ALL_READERS_WITH_BOOK, mapToReadersWithBook(readersWithBook));
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve readers with books, due to DB internal error: {}", ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve readers with books", ex);
        }

        return readersWithBook;
    }

    private RowMapper<Book> mapToReadersWithBook(Map<Reader, List<Book>> readersWithBook) {
        return (rs, rowNum) -> {
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
        };
    }

    @Override
    public void deleteById(long id) {
        try {
            jdbcTemplate.update("delete from reader where id = ?", id);
        } catch (DataAccessException ex) {
            logger.error("Failed to delete reader with ID {}. Error details: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to delete reader with ID " + id, ex);
        }
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
