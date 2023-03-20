package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Document;


import java.time.LocalDateTime;


@Document(collection = "events", schemaVersion = "1.0")
public class DateAttribute extends Attribute<String>{

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @JsonCreator
    public DateAttribute(@JsonProperty("ID") int ID,
                         @JsonProperty("name") String name,
                         @JsonProperty("description") String description,
                         @JsonProperty("type") Class<String> type,
                         @JsonProperty("startDateTime") LocalDateTime startDateTime,
                         @JsonProperty("endDateTime") LocalDateTime endDateTime) {
        super(ID, name, description, type);
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
