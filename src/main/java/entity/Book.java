package entity;

public class Book {
    private long id;
    private String name;
    private String author;
    private long readerId;

    public Book(long id, String name, String author, long readerId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.readerId = readerId;
    }

    public Book(long id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public void setId(long id) {
        if (this.id == 0) {
            this.id = id;
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return this.readerId == 0 ? "Book{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", author = '" + author + '}' :
                "Book{" +
                        "id = " + id +
                        ", name = '" + name + '\'' +
                        ", author = '" + author + '\'' +
                        ", readerId = " + readerId +
                        '}';
    }
}