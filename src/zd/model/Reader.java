package zd.model;

public class Reader {
    private int id;
    private String name;

    private static int count = 0;

    public Reader(String name) {
        count++;
        this.id = count;
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
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
