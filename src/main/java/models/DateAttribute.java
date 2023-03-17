package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public class DateAttribute extends Attribute<String>{

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public DateAttribute(int ID, String name, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(ID, name, description, String.class);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
