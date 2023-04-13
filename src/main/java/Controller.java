import io.javalin.Javalin;
import models.*;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class Controller {
    public static void main(String[] args) throws GeneralSecurityException {

        Database database = new Database(); // creates a new database object

        // starts the backend on port 5000, can be whatever we want in the end.
        Javalin app = Javalin.create().start(50000);

        app.get("/makeCalendarEvent", ctx -> {
            Event event = database.getEvent("test");
            ctx.json(event);
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
