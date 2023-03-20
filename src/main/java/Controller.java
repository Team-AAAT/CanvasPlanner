import io.javalin.Javalin;
import models.Attribute;
import models.DateAttribute;
import models.Event;
import models.Database;
import net.fortuna.ical4j.model.Calendar;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;

public class Controller {
    public static void main(String[] args) throws GeneralSecurityException {

        Database database = new Database(); // creates a new database object

        // starts the backend on port 5000, can be whatever we want in the end.
        Javalin app = Javalin.create().start(5000);

        app.post("/makeCalendarEvent", ctx ->{ // this is the endpoint for the frontend to send the data to

            DateAttribute dateAttribute= new DateAttribute(1, "Date", "test", String.class, LocalDateTime.now(), LocalDateTime.now()); // creates a new attribute
            Event tempEvent = new Event(1,"test", "this is a temp calendar event", dateAttribute); // creates a new event

            Attribute<Integer> tempAttribute= new Attribute<>(1, "students", 123, Integer.class); // creates a new attribute
            tempEvent.addIntAttribute(tempAttribute); // adds the attribute to the event
            database.addEvent(tempEvent); // adds the event to the database

            //return a conformaiton that the event was added
            ctx.result("Event added");
        });

        app.get("/getCalendarEvent", ctx -> { // this is the endpoint for the frontend to get the data from
            // temp code for sending a json object to the frontend, visit http://localhost:5000/getCalanderEvent to see it
            Event event = database.getEventByID(1);
            ctx.json(event);
        });

    }
}
