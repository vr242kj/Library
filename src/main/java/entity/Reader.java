package entity;

public class Reader {
    private int id;
    private String name;


    public Reader(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Reader(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "Reader{" +
                "id = " + id +
                ", name = " + name +
                '}';
    }
}
