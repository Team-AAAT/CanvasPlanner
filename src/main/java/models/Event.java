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
    private String ID;
    private String name;
    private List<Attribute<String>> stringAttributes;
    private List<Attribute<Integer>> intAttributes;
    private List<DateAttribute> dateAttributes;
    private String description;
    private Boolean completionStatus;

    @JsonCreator
    public Event(@JsonProperty("ID") String ID,
                 @JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("stringAttributes") List<Attribute<String>> stringAttributes,
                 @JsonProperty("intAttributes") List<Attribute<Integer>> intAttributes,
                 @JsonProperty("dateAttributes") List<DateAttribute> dateAttributes, 
                 @JsonProperty("completionStatus") Boolean completionStatus) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.stringAttributes = stringAttributes;
        this.intAttributes = intAttributes;
        this.dateAttributes = dateAttributes;
        this.completionStatus = completionStatus;
    }

    public Event(String name, String description, DateAttribute dateAttribute) {
        this.name = name;
        this.description = description;
        this.stringAttributes = new ArrayList<>();
        this.intAttributes = new ArrayList<>();

        this.dateAttributes = new ArrayList<>();
        this.dateAttributes.add(dateAttribute);

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

    public List<DateAttribute> getDateAttributes() {
        return dateAttributes;
    }

    public void setDateAttributes(List<DateAttribute> dateAttributes) {
        this.dateAttributes = dateAttributes;
    }

    public void addDateAttribute(DateAttribute attribute){
        this.dateAttributes.add(attribute);
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
}
