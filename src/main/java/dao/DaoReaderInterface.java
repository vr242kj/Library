package dao;

import entity.Reader;

import java.util.List;
import java.util.Optional;

public interface DaoReaderInterface {
    List<Reader> findAll ();
    Optional<Reader> findById (int id);
    Optional<Reader> save (Reader readerToSave);
}
