package im.aktive.aktive.model;

/**
 * Created by hoangtran on 18/7/14.
 */
public class ATTagValue {
    private int id;
    private String name;
    private String description;

    public ATTagValue(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
