package models;

import io.jsondb.JsonDBTemplate;
import org.jetbrains.annotations.NotNull;

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
        if (!jsonDBTemplate.collectionExists(EventList.class)) {
            jsonDBTemplate.createCollection(EventList.class);
        }
        if (!jsonDBTemplate.collectionExists(Attribute.class)) {
            jsonDBTemplate.createCollection(Attribute.class);
        }
    }

    public void addStringAttribute(@NotNull Attribute<String> attribute) {
        if (jsonDBTemplate.findOne(
                String.format("/.[name='%s']", attribute.getName()),
                Attribute.class) == null) {
            jsonDBTemplate.insert(attribute);
        }
    }

    public void addIntegerAttribute(Attribute<Integer> attribute) {
        if (jsonDBTemplate.findOne(
                String.format("/.[name='%s']", attribute.getName()),
                Attribute.class) == null) {
            jsonDBTemplate.insert(attribute);
        }
    }

    public void addEvent(@NotNull Event event) {
        jsonDBTemplate.insert(event);
    }

    public Event getEventByID(int id) {
        return jsonDBTemplate.findOne(
                String.format("/.[ID='%s']", id),
                Event.class);
    }


}
