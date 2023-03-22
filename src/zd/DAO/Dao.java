package zd.DAO;

import zd.model.Book;
import zd.model.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dao {

    public List<Book> books = new ArrayList<Book>();
    public List<Reader> readers = new ArrayList<Reader>();
    public HashMap<String, String> bookAndReader = new HashMap<String, String>();

    public Dao(){
        books.add(new Book("In Search of Lost Time", "Marcel Proust"));
        books.add(new Book( " Ulysses", "James Joyce"));
        books.add(new Book( " Don Quixote", "Miguel de Cervantes"));

        readers.add(new Reader( "Gabriel Garcia Marquez"));
        readers.add(new Reader( "F. Scott Fitzgerald"));
        readers.add(new Reader( "Herman Melville"));
    }


}
