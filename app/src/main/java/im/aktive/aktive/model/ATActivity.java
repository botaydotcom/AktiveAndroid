package im.aktive.aktive.model;

/**
 * Created by hoangtran on 15/7/14.
 */
public class ATActivity {
    int id;
    String name;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ATActivity(int id)
    {
        this(id, null);
    }

    public ATActivity(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
