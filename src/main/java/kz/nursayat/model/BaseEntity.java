package kz.nursayat.model;

public abstract class BaseEntity {
    private int id;

    public BaseEntity(int id) {
        this.id = id;
    }

    public abstract String getEntityName();
    public abstract String getFullDescription();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}