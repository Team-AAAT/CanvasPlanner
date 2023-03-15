package models;

public class Attribute<T> {
    private int ID;
    private String name;
    private T value;
    private Class<T> type;

    public Attribute(int ID, String name, T value, Class<T> type) {
        this.ID = ID;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
