package entity;

public class Reader {
    private long id;
    private String name;


    public Reader(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Reader(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id = " + id +
                ", name = " + name +
                '}';
    }
}
