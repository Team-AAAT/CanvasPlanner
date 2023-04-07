package models;

import io.jsondb.JsonDBTemplate;
import java.io.File;

public class Database {
    private static final String DB_FILES_LOCATION = "src/main/resources/database";
    private static final String DB_NAME = "models";
    private final JsonDBTemplate jsonDBTemplate;

    //todo: fix to all of the case of an already existing database file
    public Database() {
        String dbFolder = new File(DB_FILES_LOCATION).getAbsolutePath();
        jsonDBTemplate = new JsonDBTemplate(dbFolder, DB_NAME);
        if (!jsonDBTemplate.collectionExists(Event.class)) {
            jsonDBTemplate.createCollection(Event.class);
        }
    }

    public void addEvent(Event event) {
        jsonDBTemplate.insert(event);
    }

    public Event getEventByID(int id) {
        return jsonDBTemplate.findOne(
                String.format("/.[ID='%s']", id),
                Event.class);
    }
}
