package com.example.dao;

import com.example.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookDaoJdbcTemplateImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAll() {
        try {
            return jdbcTemplate.query("select * from book",
                    this::mapToBook);
        } catch (DataAccessException ex) {
            log.error("Failed to retrieval books from the database, due to DB internal error: {}", ex.getLocalizedMessage());
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
            log.error("Failed to retrieve book with id {} from the database, due to DB internal error: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve book from the database.", ex);
        }
    }

    @Override
    public Book save(Book bookToSave) {
        String SQL_INSERT = "insert into book(name, author, maxborrowtimeindays, restricted) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, bookToSave.getName());
                ps.setString(2, bookToSave.getAuthor());
                ps.setInt(3, bookToSave.getMaxBorrowTimeInDay());
                ps.setBoolean(4, bookToSave.isRestricted());
                return ps;
            }, keyHolder);
        } catch (DataAccessException ex) {
            log.error("Failed to create new book {} in DB, due to DB internal error: {}", bookToSave, ex.getLocalizedMessage());
            throw new DAOException("Failed to save new book: " + bookToSave.toString(), ex);
        }

        Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Integer.class::cast)
                .ifPresentOrElse(
                        bookToSave::setId,
                        () -> {
                            log.error("Generated ID is null for book: {}", bookToSave);
                            throw new DAOException("Failed to retrieve generated ID for book: " + bookToSave);
                        }
                );

        return bookToSave;
    }

    @Override
    public void deleteById(long id) {
        try {
            int rowsAffected = jdbcTemplate.update("delete from book where id = ?", id);

            if (rowsAffected == 0) {
                throw new DAOException("Book with ID " + id + " does not exist");
            }
        } catch (DataAccessException ex) {
            log.error("Failed to delete book with ID {}. Error details: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to delete book with ID " + id, ex);
        }
    }

    private Book mapToBook(ResultSet rs, int rowNum) {
        try {
            return new Book(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("author"),
                    rs.getInt("maxborrowtimeindays"),
                    rs.getBoolean("restricted")
            );
        } catch (SQLException e) {
            throw new DAOException("Failed map resultSet to Book object!" + "\nError details: " + e.getMessage());
        }
    }

}
