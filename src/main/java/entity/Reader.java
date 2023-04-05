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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id = " + id +
                ", name = " + name +
                '}';
    }
}
