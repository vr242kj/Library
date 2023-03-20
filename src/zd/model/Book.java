package zd.model;

public class Book {
    private int id;
    private String name;
    private String author;

    private static int count = 0;

    public Book(String name, String author) {
        count++;
        this.id = count;
        this.name = name;
        this.author = author;
    }

    public int getId() {
        return id;
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
                "id=" + id +
                ", name=" + name +
                ", author=" + author +
                '}';
    }
}
