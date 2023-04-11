package models;

import io.jsondb.annotation.Document;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Id;
//import io.jsondb.annotation.*;

@Document(collection = "Attributes", schemaVersion = "1.0")
public class Attribute<T> {

    @Id
    private String ID;
    private String name;
    private T value;
    private Class<T> type;

    @JsonCreator
    public Attribute(@JsonProperty("ID") String ID,
                     @JsonProperty("name") String name,
                     @JsonProperty("value") T value,
                     @JsonProperty("type") Class<T> type) {
        this.ID = ID;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public Attribute(String name, T value, Class<T> type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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

    public Attribute<T> cloneBlankAttribute() {
        return new Attribute<>(this.name, null, this.type);
    }
}
