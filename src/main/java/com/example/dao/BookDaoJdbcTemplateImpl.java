package com.example.dao;

import com.example.entity.Book;
import com.example.entity.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
public class BookDaoJdbcTemplateImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    public BookDaoJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final Logger logger = LoggerFactory.getLogger(BookDaoJdbcTemplateImpl.class);
    @Override
    public List<Book> findAll() {
        try {
            return jdbcTemplate.query("select * from book",
                    this::mapToBook);
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieval books from the database, due to DB internal error: {}", ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve books from the database.", ex);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            return jdbcTemplate.query("select * from book where id = ?",
                            this::mapToBook, id)
                    .stream().findFirst();
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve book with id {} from the database, due to DB internal error: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve book from the database.", ex);
        }
    }

    @Override
    public List<Book> findAllByReaderId(long readerId) {
        try {
            return jdbcTemplate.query(
                    "select * from book where readerId = ?",
                    this::mapToBook,
                    readerId
            );
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve books by readerId: {}. Error details: {}", readerId, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve books by readerId: " + readerId, ex);
        }
    }

    @Override
    public Book save(Book bookToSave) {
        String SQL_INSERT = "insert into book(name, author) values(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, bookToSave.getName());
                ps.setString(2, bookToSave.getAuthor());
                return ps;
            }, keyHolder);
        } catch (DataAccessException ex) {
            logger.error("Failed to create new book {} in DB, due to DB internal error: {}", bookToSave, ex.getLocalizedMessage());
            throw new DAOException("Failed to save new book: " + bookToSave.toString(), ex);
        }

        var generatedKeys = keyHolder.getKeys();

        if (generatedKeys != null && generatedKeys.containsKey("id")) {
            bookToSave.setId((Integer) generatedKeys.get("id"));
        } else {
            logger.error("Generated ID is null for book: {}", bookToSave);
            throw new DAOException("Failed to retrieve generated ID for book: " + bookToSave.toString());
        }

        return bookToSave;
    }

    @Override
    public void borrowBookToReader(long bookId, long readerId) {
        try {
            jdbcTemplate.update("update book set readerId = ? where id = ?",
                    readerId,
                    bookId);
        } catch (DataAccessException ex) {
            logger.error("Failed to update the book with id {} to reader with id {}, due to DB internal error: {}", bookId, readerId, ex.getLocalizedMessage());
            throw new DAOException("Failed to update the book with id " + bookId + " to reader with id " + readerId, ex);
        }
    }

    @Override
    public void returnBookToLibrary(long bookId) {
        try {
            jdbcTemplate.update("update book set readerId = null where id = ?",
                    bookId);
        } catch (DataAccessException ex) {
            logger.error("Failed to return the book with id {} to the library, due to DB internal error: {}", bookId, ex.getLocalizedMessage());
            throw new DAOException("Failed to return the book with ID " + bookId + " to the library", ex);
        }
    }

    @Override
    public Map<Book, Optional<Reader>> findAllWithReaders() {
        String SQL_FIND_ALL_WITH_READERS = """
                select
                    book.id,
                    book.name,
                    book.author,
                    book.readerId,
                    reader.name as readerName
                from book
                    left join reader on book.readerId = reader.id
                """;
        Map<Book, Optional<Reader>> booksWithReaders = new TreeMap<>(Comparator.comparing(Book::getId));

        try {
            jdbcTemplate.query(SQL_FIND_ALL_WITH_READERS, (rs, rowNum) -> {
                var book = mapToBook(rs, rowNum);
                var readerName = rs.getString("readerName");
                var reader = Optional.ofNullable(readerName).map(Reader::new);

                booksWithReaders.put(book, reader);
                return book;
            });
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve books with readers, due to DB internal error: {}", ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve books with readers", ex);
        }

        return booksWithReaders;
    }

    @Override
    public void deleteById(long id) {
        try {
            jdbcTemplate.update("delete from book where id = ?", id);
        } catch (DataAccessException ex) {
            logger.error("Failed to delete book with ID {}. Error details: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to delete book with ID " + id, ex);
        }
    }

    private Book mapToBook(ResultSet rs, int rowNum) {
        try {
            return new Book(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("author"),
                    rs.getInt("readerId")
            );
        } catch (SQLException e) {
            throw new DAOException("Failed map resultSet to Book object!" + "\nError details: " + e.getMessage());
        }
    }
}
