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
    private int ID;
    private LocalDate Date;
    private LinkedList<Event> events;

    @JsonCreator
    public EventList(@JsonProperty("ID") int ID,
                     @JsonProperty("date") LocalDate date,
                     @JsonProperty("events") LinkedList<Event> events) {
        this.ID = ID;
        this.Date = date;
        this.events = events;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public LocalDate getDate() {
        return Date;
    }

    public void setDate(LocalDate date) {
        Date = date;
    }

    public LinkedList<Event> getEvents() {
        return events;
    }

    public void setEvents(LinkedList<Event> events) {
        this.events = events;
    }

    //add event at specific index
    public void addEvent(int index, Event event) {
        this.events.add(index, event);
    }

    //add event at end of list
    public void addEvent(Event event) {
        this.events.add(event);
    }

    //remove event
    public void removeEvent(Event event) {
        this.events.remove(event);
    }
}
