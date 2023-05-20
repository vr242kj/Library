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

@Repository
public class BookDaoJdbcTemplateImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    public BookDaoJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("select * from book",
                this::mapToBook);
    }

    @Override
    public Optional<Book> findById(long id) {
        return jdbcTemplate.query("select * from book where id = ?",
                this::mapToBook, id)
                .stream().findFirst();
    }

    @Override
    public List<Book> findAllByReaderId(long readerId) {
        return jdbcTemplate.query("select * from book where readerId = ?",
                this::mapToBook,
                readerId);
    }

    @Override
    public Book save(Book bookToSave) {
        var SQL_ADD_NEW_BOOK = "insert into book(name, author) values(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(SQL_ADD_NEW_BOOK, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bookToSave.getName());
            ps.setString(2, bookToSave.getAuthor());
            return ps;
        }, keyHolder);

        var generatedId = (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("id");
        bookToSave.setId(generatedId);

        return bookToSave;
    }

    @Override
    public void borrowBookToReader(long bookId, long readerId) {
        jdbcTemplate.update("update book set readerId = ? where id = ?",
                readerId,
                bookId);
    }

    @Override
    public void returnBookToLibrary(long bookId) {
        jdbcTemplate.update("update book set readerId = null where id = ?",
                bookId);
    }

    @Override
    public Map<Book, Optional<Reader>> findAllWithReaders() {
        var SQL_FIND_ALL_WITH_READERS = """
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

        jdbcTemplate.query(SQL_FIND_ALL_WITH_READERS, (rs, rowNum) -> {
            var book = mapToBook(rs, rowNum);
            var readerName = rs.getString("readerName");
            var reader = Optional.ofNullable(readerName).map(Reader::new);

            booksWithReaders.put(book, reader);
            return book;
        });

        return booksWithReaders;
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("delete from book where id = ?", id);
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
