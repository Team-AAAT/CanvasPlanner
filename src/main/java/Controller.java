import io.javalin.Javalin;
import models.Attribute;
import models.Event;
import models.Database;
import net.fortuna.ical4j.model.Calendar;

public class Controller {
    public static void main(String[] args) {

        Database database = new Database();
        Event tempEvent = new Event(1,"test", "this is a temp calendar event"); // creates a new event
        database.addEvent(tempEvent);


        // starts the backend on port 5000, can be whatever we want in the end.
//        Javalin app = Javalin.create().start(5000);
//
//        app.post("/makeCalendarEvent", ctx ->{ // this is the endpoint for the frontend to send the data to
//            Event tempEvent = new Event(1,"test", "this is a temp calendar event"); // creates a new event
//
//            Attribute<Integer> tempAttribute= new Attribute<>(1, "Date", 123, Integer.class); // creates a new attribute
//            tempEvent.addIntAttribute(tempAttribute); // adds the attribute to the event
//
//        });
//
//        app.get("/output", ctx -> { // this is the endpoint for the frontend to get the data from
//            // temp code for sending a json object to the frontend
//            ctx.json("{ \"name\": \"John\", \"age\": 30 }}");
//        });

    }
}
