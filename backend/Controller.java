import io.javalin.Javalin;
import models.Attribute;
import models.Calendar;

public class Controller {
    public static void main(String[] args) {
        System.out.println("Server is running...");

        try (Javalin app = Javalin.create().start(8080)){
            app.post("/makeCalendarEvent", ctx ->{

                Calendar tempEvent = new Calendar(1,"test", "this is a temp calendar event");

                Attribute<Integer> tempAttribute= new Attribute<Integer>(1, "Date", 123, Integer.class);
                tempEvent.addIntAttribute(tempAttribute);

            });
        }
    }
}
