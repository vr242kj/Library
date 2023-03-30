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

   public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public long getReaderId() {
        return readerId;
    }

    public void setReaderId(long readerId) {
        this.readerId = readerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", author = '" + author + '\'' +
                ", readerId = " + readerId +
                '}';
    }
}
