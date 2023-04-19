package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsondb.annotation.Document;


import java.time.LocalDateTime;
import java.util.Objects;


@Document(collection = "Attributes", schemaVersion = "1.0")
public class DateAttribute extends Attribute<String>{

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @JsonCreator
    public DateAttribute(@JsonProperty("ID_Attribute") String ID_Attribute,
                         @JsonProperty("name") String name,
                         @JsonProperty("description") String description,
                         @JsonProperty("type") Class<String> type,
                         @JsonProperty("startDateTime") LocalDateTime startDateTime,
                         @JsonProperty("endDateTime") LocalDateTime endDateTime) {
        super(ID_Attribute, name, description, type);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

public DateAttribute(String name, String description, Class<String> type, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(name, description, String.class);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DateAttribute that = (DateAttribute) o;
        return Objects.equals(startDateTime, that.startDateTime) && Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDateTime, endDateTime);
    }
}
