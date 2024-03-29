package com.example.dao;

import com.example.entity.Reader;
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
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReaderDaoJdbcTemplateImpl implements ReaderDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Reader> findAll() {
        try {
            return jdbcTemplate.query("select * from reader",
                    this::mapToReader);
        } catch (DataAccessException ex) {
            log.error("Failed to retrieval reader from the database, due to DB internal error: {}", ex.getLocalizedMessage());
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
            log.error("Failed to retrieve reader with id {} from the database, due to DB internal error: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to retrieve reader from the database.", ex);
        }
    }

    @Override
    public Reader save(Reader readerToSave) {
        String SQL_INSERT = "insert into reader(name, birthdate) values(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, readerToSave.getName());
                ps.setDate(2, Date.valueOf(readerToSave.getBirthdate()));
                return ps;
            }, keyHolder);
        } catch (DataAccessException ex) {
            log.error("Failed to create new book {} in DB, due to DB internal error: {}", readerToSave, ex.getLocalizedMessage());
            throw new DAOException("Failed to save new book: " + readerToSave.toString(), ex);
        }

        Optional.ofNullable(keyHolder.getKeys())
                .map(keys -> keys.get("id"))
                .map(Integer.class::cast)
                .ifPresentOrElse(
                        readerToSave::setId,
                        () -> {
                            log.error("Generated ID is null for reader: {}", readerToSave);
                            throw new DAOException("Failed to retrieve generated ID for reader: " + readerToSave);
                        }
                );

        return readerToSave;
    }

    @Override
    public void deleteById(long id) {
        try {
            int rowsAffected = jdbcTemplate.update("delete from reader where id = ?", id);

            if (rowsAffected == 0) {
                throw new DAOException("Reader with ID " + id + " does not exist");
            }
        } catch (DataAccessException ex) {
            log.error("Failed to delete reader with ID {}. Error details: {}", id, ex.getLocalizedMessage());
            throw new DAOException("Failed to delete reader with ID " + id, ex);
        }
    }

    private Reader mapToReader(ResultSet rs, int rowNum) {
        try {
            Optional<Date> birthdate = Optional.ofNullable(rs.getDate("birthdate"));

            return new Reader(
                    rs.getInt("id"),
                    rs.getString("name"),
                    birthdate.map(Date::toLocalDate).orElse(null)
            );
        } catch (SQLException e) {
            throw new DAOException("Failed to map resultSet to Reader object!" + "\nError details: " + e.getMessage());
        }
    }

}
