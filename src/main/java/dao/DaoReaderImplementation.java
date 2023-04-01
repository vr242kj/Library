package dao;

import entity.Reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoReaderImplementation implements DaoReaderInterface{
    private final List<Reader> readers = new ArrayList<Reader>();

    public DaoReaderImplementation () {
        //save(new Reader( "Gabriel Garcia Marquez"));
        //save(new Reader( "F. Scott Fitzgerald"));
        //save(new Reader( "Herman Melville"));
    }

    @Override
    public List<Reader> findAll() {
        return readers;
    }

    @Override
    public Optional<Reader> findById(int id) {
        return readers.stream().filter(reader -> reader.getId() == id).findFirst();
    }

    @Override
    public Optional<Reader> save(Reader readerToSave) {
        readers.add(readerToSave);
        return Optional.of(readerToSave);
    }
}
