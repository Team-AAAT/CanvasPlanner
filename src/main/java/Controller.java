import com.google.gson.*;
import io.javalin.Javalin;
import models.*;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    public static void main(String[] args) throws GeneralSecurityException {

        Database database = new Database(); // creates a new database object

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // format for parsing dates

        // starts the backend on port 5000, can be whatever we want in the end.
        Javalin app = Javalin.create().start(50000);

        //removes CORS errors
        app.before(ctx -> ctx.header("Access-Control-Allow-Origin", "*"));

        app.get("/isReady", ctx -> {
            ctx.result("Server is ready");
        });

        app.post("/makeCalendarEvent", ctx -> {
            try {
                // Parse startDateTime and endDateTime from the request
                LocalDateTime startDateTime = LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("startDateTime")), formatter);
                LocalDateTime endDateTime = LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("endDateTime")), formatter);

                // Create a new Event object with the given name, description, and date range
                Event event = new Event(
                        ctx.queryParam("name"),
                        ctx.queryParam("description"),
                        new DateAttribute(
                                ctx.queryParam("name"),
                                ctx.queryParam("description"),
                                String.class,
                                startDateTime,
                                endDateTime),
                        false
                        );
                event.setID(UUID.randomUUID().toString());

                // Add the event to the database
                database.addEvent(event);

                // Create a list of dates within the given date range
                List<LocalDateTime> dates = Stream.iterate(startDateTime, date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(startDateTime, endDateTime.plusDays(1)))
                        .toList();

                // Add the event to the event lists for each date in the range
                for (LocalDateTime date : dates) {
                    // Check if an event list exists for the current date
                    EventList eventList = database.getEventListByDate(date.toLocalDate());
                    if (eventList == null) {
                        // If an event list doesn't exist, create a new one and add it to the database
                        eventList = new EventList(UUID.randomUUID().toString(), date.toLocalDate());
                        eventList.addEvent(event.getID());
                        database.addEventList(eventList);
                    } else {
                        // If an event list exists, add the event to it and update it in the database
                        eventList.addEvent(event.getID());
                        database.updateEventList(eventList);
                    }
                }

                // Return a success message to the client
                ctx.result("{\"status\": \"success\"}");
            } catch (Exception e) {
                // Handle any exceptions that occur and return a failure message to the client
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getCalendarEventByName", ctx -> {
            try {
                // Get the event from the database by name
                Event event = database.getEventByName(ctx.queryParam("name"));

                // Respond with the event as JSON
                ctx.json(event);
            } catch (Exception e) {
                // Print the stack trace if there's an exception
                e.printStackTrace();

                // Respond with a failure status as JSON
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/getCalendarEventByID", ctx -> {
            try {
                // Get the event from the database using the ID passed as a query parameter
                Event event = database.getEventByID(ctx.queryParam("id"));
                // Respond with the event as JSON
                ctx.json(event);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.post("/updateEvent", ctx -> {
            try {
                // Get the old event object from the database
                Event oldEvent = database.getEventByID(ctx.queryParam("id"));

                // Create the new date attribute object with the updated start and end date times
                DateAttribute dateAttribute = new DateAttribute(
                        ctx.queryParam("dateAttributeName"),
                        ctx.queryParam("dateAttributeDescription"),
                        String.class,
                        LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("startDateTime")), formatter),
                        LocalDateTime.parse(Objects.requireNonNull(ctx.queryParam("endDateTime")), formatter));

                // Parse the JSON body of the request to get the new attributes
                String jsonBody = ctx.body();
                JsonArray jsonArray = JsonParser.parseString(jsonBody).getAsJsonArray();

                List<Attribute<String>> stringAttributes = new ArrayList<>();
                List<Attribute<Integer>> integerAttributes = new ArrayList<>();

                // Iterate through the JSON array and add attributes to the appropriate list based on whether they're integers or strings
                for (JsonElement element : jsonArray) {
                    JsonObject jsonObject = element.getAsJsonObject();

                    String name = jsonObject.get("name").getAsString();
                    String id = jsonObject.get("id").getAsString();
                    JsonElement valueElement = jsonObject.get("value");


                    JsonPrimitive valuePrimitive = valueElement.getAsJsonPrimitive();
                    if (valuePrimitive.isNumber()) {
                        Integer value = valuePrimitive.getAsInt();
                        Attribute<Integer> jsonAttribute = new Attribute<>(
                                id,
                                name,
                                value,
                                Integer.class);
                        database.updateIntegerAttribute(jsonAttribute.cloneBlankAttributeWithID());
                        integerAttributes.add(jsonAttribute);
                    }else{
                        String value = valuePrimitive.getAsString();
                        Attribute<String> jsonAttribute = new Attribute<>(
                                id,
                                name,
                                value,
                                String.class);
                        database.updateStringAttribute(jsonAttribute.cloneBlankAttributeWithID());
                        stringAttributes.add(jsonAttribute);
                    }

                }

                // Create the new event object with the updated attributes
                Event newEvent = new Event(ctx.queryParam("name"), ctx.queryParam("description"), dateAttribute, Boolean.valueOf(ctx.queryParam("completionStatus")));
                newEvent.setID(oldEvent.getID());
                newEvent.setStringAttributes(stringAttributes);
                newEvent.setIntAttributes(integerAttributes);

                // Remove the old event from all event lists in the database
                List<LocalDateTime> oldDates = Stream.iterate(oldEvent.getDateAttributes().getStartDateTime(), date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(oldEvent.getDateAttributes().getStartDateTime(), oldEvent.getDateAttributes().getEndDateTime().plusDays(1)))
                        .toList();

                for (LocalDateTime date : oldDates) {
                    EventList eventList = database.getEventListByDate(date.toLocalDate());
                    if (eventList != null) {
                        eventList.removeEvent(oldEvent.getID());
                        database.updateEventList(eventList);
                    }
                }

                // Add the new event to all event lists in the database
                List<LocalDateTime> newDates = Stream.iterate(newEvent.getDateAttributes().getStartDateTime(), date -> date.plusDays(1))
                        .limit(ChronoUnit.DAYS.between(newEvent.getDateAttributes().getStartDateTime(), newEvent.getDateAttributes().getEndDateTime().plusDays(1)))
                        .toList();

                for (LocalDateTime date : newDates) {
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

        app.post("/updateListOrder", ctx -> {
            try {
                // Get the event list for the given date
                EventList eventList = database.getEventListByDate(LocalDate.parse(Objects.requireNonNull(ctx.queryParam("date")), formatter));

                // Get the event ID and the new position from the request
                String id = ctx.queryParam("id");
                String position = ctx.queryParam("position");

                // Update the position of the event in the list
                eventList.getEventIdList().remove(id); // Remove the event from its current position
                eventList.getEventIdList().add(Integer.parseInt(Objects.requireNonNull(position)), id); // Add the event to its new position

                // Update the event list in the database
                database.updateEventList(eventList);

                ctx.result("{\"status\": \"success\"}");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.result("{\"status\": \"failure\"}");
            }
        });

        app.get("/monthPriority", ctx -> {
            try {
                // Parse the month parameter from the query string
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
                YearMonth yearMonth = YearMonth.parse(Objects.requireNonNull(ctx.queryParam("month")), monthFormatter);

                // Get the dates for the month
                List<LocalDate> dates = yearMonth.atDay(1) // First day of month
                        .datesUntil(yearMonth.atEndOfMonth().plusDays(1)) // Generate stream of dates until last day of month
                        .toList();

                List<Event> events = new ArrayList<>();

                // Iterate through the dates and add the non-completed events to the events list
                for (LocalDate localDate : dates) {
                    EventList eventList = database.getEventListByDate(localDate);

                    if (eventList == null) {
                        continue;
                    }

                    for (String eventID : eventList.getEventIdList()) {
                        Event event = database.getEventByID(eventID);
                        if (event == null) {
                            continue;
                        }
                        if (event.getCompletionStatus()) {
                            continue;
                        }
                        events.add(event);
                    }
                }

                // Create a frequency map to count the occurrences of each element in the original list
                Map<Event, Integer> frequencyMap = new HashMap<>();
                for (Event event : events) {
                    frequencyMap.put(event, frequencyMap.getOrDefault(event, 0) + 1);
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
    }
}
