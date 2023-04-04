package entity;

public class Book {
    private int id;
    private String name;
    private String author;
    private int readerId;

    public Book(int id, String name, String author, int readerId) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.readerId = readerId;
    }

   public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReaderId() {
        return readerId;
    }

    public void setReaderId(int readerId) {
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
