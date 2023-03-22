package zd.model;

import java.util.concurrent.atomic.AtomicLong;

public class Reader {
    private long id;
    private String name;

    private static AtomicLong count = new AtomicLong(0);

    public Reader(String name) {
        this.id = count.incrementAndGet();
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
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
