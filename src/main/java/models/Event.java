package models;

import io.jsondb.annotation.Document;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Document(collection = "events", schemaVersion = "1.0")
public class Event {

    @Id
    private String ID;
    private String name;
    private List<Attribute<String>> stringAttributes;
    private List<Attribute<Integer>> intAttributes;
    private DateAttribute dateAttributes;
    private String description;
    private Boolean completionStatus;

    @JsonCreator
    public Event(@JsonProperty("ID") String ID,
                 @JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("stringAttributes") List<Attribute<String>> stringAttributes,
                 @JsonProperty("intAttributes") List<Attribute<Integer>> intAttributes,
                 @JsonProperty("dateAttributes") DateAttribute dateAttributes,
                 @JsonProperty("completionStatus") Boolean completionStatus) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.stringAttributes = stringAttributes;
        this.intAttributes = intAttributes;
        this.dateAttributes = dateAttributes;
    }

    public Event(String name, String description, DateAttribute dateAttribute) {
        this.name = name;
        this.description = description;
        this.stringAttributes = new ArrayList<>();
        this.intAttributes = new ArrayList<>();

        this.dateAttributes = dateAttribute;

        this.completionStatus = false;
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

    public DateAttribute getDateAttributes() {
        return dateAttributes;
    }

    public void setDateAttribute(DateAttribute dateAttributes) {
        this.dateAttributes = dateAttributes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(Boolean completionStatus) {
        this.completionStatus = completionStatus;
    }

    @Override
    public String toString() {
        return "Event{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", stringAttributes=" + stringAttributes +
                ", intAttributes=" + intAttributes +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return ID.equals(event.ID) && name.equals(event.name) && Objects.equals(stringAttributes, event.stringAttributes) && Objects.equals(intAttributes, event.intAttributes) && dateAttributes.equals(event.dateAttributes) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, stringAttributes, intAttributes, dateAttributes, description);
    }
}
