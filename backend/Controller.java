import io.javalin.Javalin;
import models.Attribute;
import models.Calendar;

public class Controller {
    public static void main(String[] args) {

        Javalin app = Javalin.create().start(5000);

        app.post("/makeCalendarEvent", ctx ->{

            Calendar tempEvent = new Calendar(1,"test", "this is a temp calendar event");

            Attribute<Integer> tempAttribute= new Attribute<>(1, "Date", 123, Integer.class);
            tempEvent.addIntAttribute(tempAttribute);

        });

        app.get("/output", ctx -> {
            // some code
            ctx.json("{ \"name\": \"John\", \"age\": 30 }}");
        });

        System.out.println("Server is running...");
    }
}
