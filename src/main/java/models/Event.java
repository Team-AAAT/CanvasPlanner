package models;

import io.jsondb.annotation.Document;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "events", schemaVersion = "1.0")
public class Event {

    @Id
    private int ID;
    private String name;
    private List<Attribute<String>> stringAttributes;
    private List<Attribute<Integer>> intAttributes;
    private String description;

    @JsonCreator
    public Event(@JsonProperty("ID") int ID,
                 @JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("stringAttributes") List<Attribute<String>> stringAttributes,
                 @JsonProperty("intAttributes") List<Attribute<Integer>> intAttributes) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.stringAttributes = stringAttributes;
        this.intAttributes = intAttributes;
    }

    public Event(int ID, String name, String description) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.stringAttributes = new ArrayList<>();
        this.intAttributes = new ArrayList<>();
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

    public List<Attribute<String>> getStringAttributes() {
        return stringAttributes;
    }

    public void setStringAttributes(List<Attribute<String>> stringAttributes) {
        this.stringAttributes = stringAttributes;
    }

    public void addStringAttribute(Attribute<String> attribute){
        this.stringAttributes.add(attribute);
    }

    public List<Attribute<Integer>> getIntAttributes() {
        return intAttributes;
    }

    public void setIntAttributes(List<Attribute<Integer>> intAttributes) {
        this.intAttributes = intAttributes;
    }

    public void addIntAttribute(Attribute<Integer> attribute){
        this.intAttributes.add(attribute);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", stringAttributes=" + stringAttributes +
                ", intAttributes=" + intAttributes +
                ", description='" + description + '\'' +
                '}';
    }
}
