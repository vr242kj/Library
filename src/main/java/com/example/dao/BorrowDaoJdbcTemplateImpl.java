package com.example.dao;

import com.example.entity.Borrow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BorrowDaoJdbcTemplateImpl implements BorrowDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Borrow> findAll() {
        String SQL_QUERY = """
            select
                borrow.id,
                borrow.bookId,
                borrow.readerId,
                borrow.borrowStartDate,
                borrow.borrowEndDate,
                borrow.borrowStartDate + book.maxBorrowTimeInDays as expectedReturn
            from
                borrow 
            inner join 
                book on borrow.bookId = book.id
        """;

        try {
            return jdbcTemplate.query(SQL_QUERY, this::mapToBorrow);
        } catch (DataAccessException ex) {
            log.error("Failed to retrieval borrows from the database, due to DB internal error: {}", ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve borrows from the database.", ex);
        }
    }

    @Override
    public Optional<Borrow> findById(long id) {
        String SQL_QUERY = """
            select 
                borrow.id,
                borrow.bookId,
                borrow.readerId,
                borrow.borrowStartDate, borrow.borrowEndDate,
                borrow.borrowStartDate + book.maxBorrowTimeInDays as expectedReturn
            from 
                borrow
            inner join 
                book on borrow.bookId = book.id
            where borrow.id = ?
        """;

        try {
            return jdbcTemplate.query(SQL_QUERY, this::mapToBorrow, id)
                    .stream()
                    .findFirst();
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve borrow with id {} from the database, due to DB internal error: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve borrow from the database.", ex);
        }
    }

    @Override
    public Borrow save(Borrow borrowToSave) {
        String SQL_INSERT = "insert into borrow(bookId, readerId, borrowStartDate) values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, borrowToSave.getBookId());
                ps.setLong(2, borrowToSave.getReaderId());
                ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException ex) {
            log.error("Failed to create new borrow {} in DB, due to DB internal error: {}", borrowToSave, ex.getLocalizedMessage());
            throw new DAOException("Failed to save new borrow: " + borrowToSave.toString(), ex);
        }

        Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Integer.class::cast)
                .ifPresentOrElse(
                        borrowToSave::setId,
                        () -> {
                            log.error("Generated ID is null for borrow: {}", borrowToSave);
                            throw new DAOException("Failed to retrieve generated ID for borrow: " + borrowToSave);
                        }
                );

        return borrowToSave;
    }

    @Override
    public void returnBookToLibraryByBorrowId(long id, LocalDate currentDate) {
        try {
            jdbcTemplate.update("update borrow set borrowenddate = ? where id = ?", currentDate, id);
        } catch (DataAccessException ex) {
            log.error("Failed to return the book for borrow with id {} to the library, due to DB internal error: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to return the book for borrow with ID " + id + " to the library", ex);
        }
    }

    @Override
    public Optional<Borrow> findBorrowByBookId(long bookId) {
        String SQL_QUERY = """
            select 
                borrow.id,
                borrow.bookId,
                borrow.readerId,
                borrow.borrowStartDate, borrow.borrowEndDate,
                borrow.borrowStartDate + book.maxBorrowTimeInDays as expectedReturn
            from 
                borrow
            inner join 
                book on borrow.bookId = book.id
            where borrow.bookId = ? and borrow.borrowenddate is null
        """;

        try {
            return jdbcTemplate.query(SQL_QUERY, this::mapToBorrow, bookId)
                    .stream()
                    .findFirst();
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve borrow with book id {} from the database, due to DB internal error: {}", bookId, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve borrow by book id from the database.", ex);
        }
    }

    @Override
    public List<Borrow> findAllBorrowsByReaderId(long readerId) {
        String SQL_QUERY = """
            select 
                borrow.id,
                borrow.bookId,
                borrow.readerId,
                borrow.borrowStartDate, borrow.borrowEndDate,
                borrow.borrowStartDate + book.maxBorrowTimeInDays as expectedReturn
            from 
                borrow
            inner join 
                book on borrow.bookId = book.id
            where borrow.readerId = ? and borrow.borrowenddate is null
        """;

        try {
            return jdbcTemplate.query(SQL_QUERY, this::mapToBorrow, readerId);
        } catch (DataAccessException ex) {
            log.error("Failed to retrieve borrow with reader id {} from the database, due to DB internal error: {}", readerId, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve borrow by reader id from the database.", ex);
        }
    }

    private Borrow mapToBorrow(ResultSet rs, int rowNum) {
        try {
            Optional<Date> borrowStartDate = Optional.ofNullable(rs.getDate("borrowStartDate"));
            Optional<Date> borrowEndDate = Optional.ofNullable(rs.getDate("borrowEndDate"));
            Optional<Date> expectedReturn = Optional.ofNullable(rs.getDate("expectedReturn"));

            return new Borrow(
                    rs.getInt("id"),
                    rs.getInt("bookId"),
                    rs.getInt("readerId"),
                    borrowStartDate.map(Date::toLocalDate).orElse(null),
                    borrowEndDate.map(Date::toLocalDate).orElse(null),
                    expectedReturn.map(Date::toLocalDate).orElse(null)
            );
        } catch (SQLException e) {
            throw new DAOException("Failed map resultSet to Borrow object!" + "\nError details: " + e.getMessage());
        }
    }

}
