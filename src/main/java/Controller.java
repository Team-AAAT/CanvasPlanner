import io.javalin.Javalin;
import models.Attribute;
import models.DateAttribute;
import models.Event;
import models.Database;
import net.fortuna.ical4j.model.Calendar;
import java.time.LocalDateTime;

public class Controller {
    public static void main(String[] args) {

        Database database = new Database(); // creates a new database object

        // starts the backend on port 5000, can be whatever we want in the end.
        Javalin app = Javalin.create().start(5000);

        app.post("/makeCalendarEvent", ctx ->{ // this is the endpoint for the frontend to send the data to

            DateAttribute dateAttribute= new DateAttribute(1, "Date", "test", LocalDateTime.now(), LocalDateTime.now()); // creates a new attribute
            Event tempEvent = new Event(1,"test", "this is a temp calendar event", dateAttribute); // creates a new event

            Attribute<Integer> tempAttribute= new Attribute<>(1, "Date", 123, Integer.class); // creates a new attribute
            tempEvent.addIntAttribute(tempAttribute); // adds the attribute to the event
            database.addEvent(tempEvent); // adds the event to the database
        });

        app.get("/output", ctx -> { // this is the endpoint for the frontend to get the data from
            // temp code for sending a json object to the frontend, visit http://localhost:5000/output to see it
            ctx.json("{ \"name\": \"John\", \"age\": 30 }}");
        });

    }
}
