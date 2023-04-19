import com.google.gson.*;
import io.javalin.Javalin;
import models.*;

import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

public class Controller {
    public static void main(String[] args) throws GeneralSecurityException {

        Database database = new Database(); // creates a new database object

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // starts the backend on port 5000, can be whatever we want in the end.
        Javalin app = Javalin.create().start(50000);

        app.post("/makeCalendarEvent", ctx -> {
            try {
                LocalDateTime startDateTime = LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("startDateTime")), formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("endDateTime")), formatter);

                Event event = new Event(ctx.queryParam("name"), ctx.queryParam("description"), new DateAttribute(ctx.queryParam("name"), ctx.queryParam("description"), String.class, startDateTime, endDateTime));
                event.setID(UUID.randomUUID().toString());
                database.addEvent(event);


                List<LocalDateTime> dates = Stream.iterate(startDateTime, date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(startDateTime, endDateTime.plusDays(1)))
                        .toList();

                for (LocalDateTime date : dates) {
                    EventList eventList = database.getEventListByDate(date.toLocalDate());
                    if (eventList == null) {
                        eventList = new EventList(UUID.randomUUID().toString(), date.toLocalDate());
                        eventList.addEvent(event.getID());
                        database.addEventList(eventList);
                    } else {
                        eventList.addEvent(event.getID());
                        database.updateEventList(eventList);
                    }
                }

                ctx.result("{\"status\": \"success\"}");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getCalendarEventByName", ctx -> {
            try {
                Event event = database.getEventByName(ctx.queryParam("name"));
                ctx.json(event);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getCalendarEventByID", ctx -> {
            try {
                Event event = database.getEventByID(ctx.queryParam("id"));
                ctx.json(event);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        //updateEvent
        app.post("/updateEvent", ctx -> {
            try {
                Event oldEvent = database.getEventByID(ctx.queryParam("id"));

                DateAttribute dateAttribute = new DateAttribute(
                        ctx.queryParam("dateAttributeName"),
                        ctx.queryParam("dateAttributeDescription"),
                        String.class,
                        LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("startDateTime")), formatter),
                        LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("endDateTime")), formatter));

                String jsonBody = ctx.body();
                JsonElement jsonElement = JsonParser.parseString(jsonBody);
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                List<Attribute<String>> stringAttributes = new ArrayList<>();
                List<Attribute<Integer>> integerAttributes = new ArrayList<>();

                //iterate through json body and add attributes to list
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();

                    String name = jsonObject.get("name").getAsString();
                    JsonElement valueElement = jsonObject.get("value");

                    JsonPrimitive valuePrimitive = valueElement.getAsJsonPrimitive();
                    if (valuePrimitive.isNumber()) {
                        Integer value = (Integer) valuePrimitive.getAsInt();
                        Attribute<Integer> jsonAttribute = new Attribute<>(
                                UUID.randomUUID().toString(),
                                jsonObject.get("name").getAsString(),
                                value,
                                Integer.class);
                        database.addIntegerAttribute(jsonAttribute.cloneBlankAttribute());
                        integerAttributes.add(jsonAttribute);
                    }else{
                        String value = valuePrimitive.getAsString();
                        Attribute<String> jsonAttribute = new Attribute<>(
                                UUID.randomUUID().toString(),
                                jsonObject.get("name").getAsString(),
                                value,
                                String.class);
                        database.addStringAttribute(jsonAttribute.cloneBlankAttribute());
                        stringAttributes.add(jsonAttribute);
                    }

                }

                Event newEvent = new Event(ctx.queryParam("name"), ctx.queryParam("description"), dateAttribute);
                newEvent.setID(oldEvent.getID());
                newEvent.setStringAttributes(stringAttributes);
                newEvent.setIntAttributes(integerAttributes);

                List<LocalDateTime> dates = Stream.iterate(oldEvent.getDateAttributes().getStartDateTime(), date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(oldEvent.getDateAttributes().getStartDateTime(), oldEvent.getDateAttributes().getEndDateTime().plusDays(1)))
                        .toList();

                for (LocalDateTime date : dates) {
                    EventList eventList = database.getEventListByDate(date.toLocalDate());
                    if (eventList != null) {
                        eventList.removeEvent(oldEvent.getID());
                        database.updateEventList(eventList);
                    }
                }

                dates = Stream.iterate(newEvent.getDateAttributes().getStartDateTime(), date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(newEvent.getDateAttributes().getStartDateTime(), newEvent.getDateAttributes().getEndDateTime().plusDays(1)))
                        .toList();

                for (LocalDateTime date : dates) {
                    EventList eventList = database.getEventListByDate(date.toLocalDate());
                    if (eventList == null) {
                        eventList = new EventList(UUID.randomUUID().toString(), date.toLocalDate());
                        eventList.addEvent(newEvent.getID());
                        database.addEventList(eventList);
                    } else {
                        eventList.addEvent(newEvent.getID());
                        database.updateEventList(eventList);
                    }
                }

                database.updateEvent(newEvent);

                ctx.result("{\"status\": \"success\"}");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getDayEvents", ctx -> {
            try {
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

        //updateListOrder
        app.post("/updateListOrder", ctx -> {
            try {
                EventList eventList = database.getEventListByDate(LocalDate.parse(Objects.requireNonNull(ctx.queryParam("date")), formatter));

                String id = ctx.queryParam("id");
                String position = ctx.queryParam("position");

                //changes the position of the event in the list
                eventList.getEventIdList().remove(id);
                eventList.getEventIdList().add(Integer.parseInt(Objects.requireNonNull(position)), id);

                database.updateEventList(eventList);
                ctx.result("{\"status\": \"success\"}");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        //monthPriority
        app.get("/monthPriority", ctx -> {
            try {
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
                YearMonth yearMonth = YearMonth.parse(Objects.requireNonNull(ctx.queryParam("month")), monthFormatter);

                // Get the first day of the month
                LocalDate date = yearMonth.atDay(1);

                // Create an empty list to hold the dates
                List<LocalDate> dates = new ArrayList<>();

                // Loop through the days of the month and add them to the list
                while (!date.isAfter(yearMonth.atEndOfMonth())) {
                    dates.add(date);
                    date = date.plusDays(1);
                }

                List<Event> events = new ArrayList<>();

                for (LocalDate localDate : dates) {
                    EventList eventList = database.getEventListByDate(localDate);
                    if (eventList != null) {
                        for (String eventID : eventList.getEventIdList()) {
                            events.add(database.getEventByID(eventID));
                        }
                    }
                }

                // Create a frequency map to count the occurrences of each element in the original list
                Map<Event, Integer> frequencyMap = new HashMap<>();
                for (Event event : events) {
                    Integer freq = frequencyMap.getOrDefault(event, 0) + 1;
                    frequencyMap.put(event, freq);
                }


                // Create a set of distinct elements from the original list and sort them by frequency
                Set<Event> resultSet = new TreeSet<>((a, b) -> frequencyMap.get(b) - frequencyMap.get(a));
                resultSet.addAll(events);

                ctx.json(resultSet);

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
