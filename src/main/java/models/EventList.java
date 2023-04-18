package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.time.LocalDate;
import java.util.LinkedList;

@Document(collection = "eventLists", schemaVersion = "1.0")
public class EventList {
    @Id
    private String ID;
    private LocalDate Date;
    private LinkedList<String> eventIdList;

    @JsonCreator
    public EventList(@JsonProperty("ID") String ID,
                     @JsonProperty("date") LocalDate date,
                     @JsonProperty("events") LinkedList<String> events) {
        this.ID = ID;
        this.Date = date;
        this.eventIdList = events;
    }

    public EventList(String ID, LocalDate date) {

        this.Date = date;
        this.eventIdList = new LinkedList<>();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public LinkedList<String> getEventIdList() {
        return eventIdList;
    }

    public void setEventIdList(LinkedList<String> eventIdList) {
        this.eventIdList = eventIdList;
    }

    //add event at specific index
    public void addEvent(int index, String event) {
        this.eventIdList.add(index, event);
    }

    //add event at end of list
    public void addEvent(String event) {
        this.eventIdList.add(event);
    }

    //remove event
    public void removeEvent(String event) {
        this.eventIdList.remove(event);
    }
}
