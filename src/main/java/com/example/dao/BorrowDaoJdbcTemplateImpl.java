package com.example.dao;

import com.example.entity.Borrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

@Repository
public class BorrowDaoJdbcTemplateImpl implements BorrowDao {

    private static final Logger logger = LoggerFactory.getLogger(BookDaoJdbcTemplateImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public BorrowDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value( "${library.minAgeForRestrictedBooks:18}" )
    private int minAgeForRestrictedBooks;

    @Override
    public List<Borrow> findAll() {
        try {
            return jdbcTemplate.query("select * from borrow",
                    this::mapToBorrow);
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieval borrows from the database, due to DB internal error: {}", ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve borrows from the database.", ex);
        }
    }

    @Override
    public Optional<Borrow> findById(long id) {
        try {
            return jdbcTemplate.query("select * from borrow where id = ?",
                            this::mapToBorrow, id)
                    .stream().findFirst();
        } catch (DataAccessException ex) {
            logger.error("Failed to retrieve borrow with id {} from the database, due to DB internal error: {}", id, ex.getLocalizedMessage());
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
            logger.error("Failed to create new borrow {} in DB, due to DB internal error: {}", borrowToSave, ex.getLocalizedMessage());
            throw new DAOException("Failed to save new borrow: " + borrowToSave.toString(), ex);
        }

        Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Integer.class::cast)
                .ifPresentOrElse(
                        borrowToSave::setId,
                        () -> {
                            logger.error("Generated ID is null for borrow: {}", borrowToSave);
                            throw new DAOException("Failed to retrieve generated ID for borrow: " + borrowToSave);
                        }
                );

        return borrowToSave;
    }

    @Override
    public Optional<Integer> countAllByReaderId(long readerId) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "select count(id) from borrow where readerId = ? and borrowEndDate is null", Integer.class, readerId);

            return Optional.ofNullable(count);
        } catch (DataAccessException ex) {
            logger.error("Failed to count borrows by readerId: {}. Error details: {}", readerId, ex.getLocalizedMessage());
            throw new DAOException("Failed to count borrows by readerId: " + readerId, ex);
        }
    }

    @Override
    public Optional<Integer> isReaderExpiredMaximumBorrowedTime(long readerId) {
        String SQL_COUNT_READERS_WITH_EXPIRED_TIME = """
                select 
                    count(DISTINCT borrow.id)
                from borrow 
                    inner join book on borrow.bookId = book.id
                where borrow.borrowEndDate IS NULL and borrow.borrowStartDate + book.maxBorrowTimeInDays < CURRENT_DATE
                  and readerId = ?;
                """;

        try {
            Integer countDistinctBorrowId = jdbcTemplate.queryForObject(
                    SQL_COUNT_READERS_WITH_EXPIRED_TIME,
                    Integer.class,
                    readerId);

            return Optional.ofNullable(countDistinctBorrowId);
        } catch (DataAccessException ex) {
            logger.error("Failed to count borrows by readerId: {}. Error details: {}", readerId, ex.getLocalizedMessage());
            throw new DAOException("Failed to count borrows by readerId: " + readerId, ex);
        }
    }

    @Override
    public Optional<Integer> isAdultToBorrowRestrictedBook(long readerId) {
        String SQL_COUNT_READERS_WITH_EXPIRED_TIME = """
                select
                    date_part('year', age(current_date, reader.birthdate)) - ?
                from reader
                where reader.id = ?;
                """;

        try {
            Integer countDifference =  jdbcTemplate.queryForObject(SQL_COUNT_READERS_WITH_EXPIRED_TIME,
                            Integer.class,
                            minAgeForRestrictedBooks,
                            readerId);

            return Optional.ofNullable(countDifference);
        } catch (DataAccessException ex) {
            logger.error("Failed to count borrows by readerId: {}. Error details: {}", readerId, ex.getLocalizedMessage());
            throw new DAOException("Failed to count borrows by readerId: " + readerId, ex);
        }
    }

    @Override
    public Optional<Integer> isBookAvailable(long bookId) {
        try {
            Integer countBorrowId = jdbcTemplate.queryForObject(
            "select count(id) from borrow where bookId = ? and borrowEndDate is null ", Integer.class, bookId);

            return Optional.ofNullable(countBorrowId);
        } catch (DataAccessException ex) {
            logger.error("Failed to count borrows by readerId: {}. Error details: {}", bookId, ex.getLocalizedMessage());
            throw new DAOException("Failed to count borrows by readerId: " + bookId, ex);
        }
    }

    @Override
    public void deleteById(long id) {
        try {
            int rowsAffected = jdbcTemplate.update("delete from borrow where id = ?", id);

            if (rowsAffected == 0) {
                throw new DAOException("Borrow with ID " + id + " does not exist");
            }
        } catch (DataAccessException ex) {
            logger.error("Failed to delete borrow with ID {}. Error details: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to delete borrow with ID " + id, ex);
        }
    }

    private Borrow mapToBorrow(ResultSet rs, int rowNum) {
        try {
            Optional<Date> borrowStartDate = Optional.ofNullable(rs.getDate("borrowStartDate"));
            Optional<Date> borrowEndDate = Optional.ofNullable(rs.getDate("borrowEndDate"));

            return new Borrow(
                    rs.getInt("id"),
                    rs.getInt("bookId"),
                    rs.getInt("readerId"),
                    borrowStartDate.map(Date::toLocalDate).orElse(null),
                    borrowEndDate.map(Date::toLocalDate).orElse(null)
            );
        } catch (SQLException e) {
            throw new DAOException("Failed map resultSet to Borrow object!" + "\nError details: " + e.getMessage());
        }
    }

}
