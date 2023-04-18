import io.javalin.Javalin;
import models.*;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Controller {
    public static void main(String[] args) throws GeneralSecurityException {

        Database database = new Database(); // creates a new database object

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        // starts the backend on port 5000, can be whatever we want in the end.
        Javalin app = Javalin.create().start(50000);

        app.post("/makeCalendarEvent", ctx -> {
            try{
                LocalDateTime startDateTime = LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("startDateTime")), formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("endDateTime")), formatter);

                Event event = new Event(ctx.queryParam("name"), ctx.queryParam("description"), new DateAttribute(ctx.queryParam("name"), ctx.queryParam("description"), String.class, startDateTime, endDateTime));
                event.setID(UUID.randomUUID().toString());
                database.addEvent(event);

                EventList eventList = database.getEventListByDate(startDateTime.toLocalDate());
                if (eventList == null) {
                    eventList = new EventList(UUID.randomUUID().toString(), startDateTime.toLocalDate());
                    eventList.addEvent(event.getID());
                    database.addEventList(eventList);
                }
                else {
                    eventList.addEvent(event.getID());
                    database.updateEventList(eventList);
                }

                ctx.result("{\"status\": \"success\"}");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getCalendarEvent", ctx -> {
            try{
                Event event = database.getEventByName(ctx.queryParam("name"));
                ctx.json(event);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getDayEvents", ctx -> {
            try{
                EventList eventList = database.getEventListByDate(LocalDate.parse(Objects.requireNonNull(ctx.queryParam("date")), formatter));
                List<Event> events = new ArrayList<>();

                if (eventList != null) {
                    for (String eventID : eventList.getEventIdList()) {
                        events.add(database.getEventByID(eventID));
                    }
                }
                ctx.json(events);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

//        app.post("/makeCalendarEvent", ctx ->{ // this is the endpoint for the frontend to send the data to
//
//            DateAttribute dateAttribute= new DateAttribute("Date", "test", String.class, LocalDateTime.now(), LocalDateTime.now()); // creates a new attribute
//
//            Attribute<Integer> tempAttribute= new Attribute<>("students", 123, Integer.class); // creates a new attribute
//            database.addIntegerAttribute(tempAttribute.cloneBlankAttribute());
//
//            Event tempEvent = new Event("test", "this is a temp calendar event", dateAttribute); // creates a new event
//            tempEvent.addIntAttribute(tempAttribute); // adds the attribute to the event
//            database.addEvent(tempEvent); // adds the event to the database
//
//            EventList tempEventList = new EventList(LocalDate.now()); // creates a new event list
//            tempEventList.addEvent(tempEvent); // adds the event to the event list
//            database.addEventList(tempEventList); // adds the event list to the database
//
//            //return a conformation that the event was added
//            ctx.result("Event added");
//        });
//
//        app.get("/getCalendarEvent", ctx -> { // this is the endpoint for the frontend to get the data from
//            // temp code for sending a json object to the frontend, visit http://localhost:5000/getCalanderEvent to see it
//            Event event = database.getEventByName("test");
//            ctx.json(event);
//        });

    }
}
