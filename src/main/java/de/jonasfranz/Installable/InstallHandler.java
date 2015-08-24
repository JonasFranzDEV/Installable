package de.jonasfranz.Installable;

import lombok.AccessLevel;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * Diese Datei unterliegt dem Urheberrecht und darf nur mit der Genehmigung des Autors verwendet, bearbeitet, veröffentlicht oder weitergegeben werden.
 * Autor des Quelltexts: ${user}
 * Sollten Zeilen bearbeitet oder hinzugefügt werden, muss dies per Kommentar gekennzeichnet werden.
 * Dieser Hinweis darf NICHT ENTFERNT werden!
 * Erstelldatum: ${date}
 * Projekt: Installable
 */
public abstract class InstallHandler {
    @Getter
    Class<?> forClass;
    @Getter(AccessLevel.PUBLIC)
    protected final LinkedHashMap<String, InstallField> context = new LinkedHashMap<>();
    protected final LinkedHashMap<Field, InstallField> toInstallField = new LinkedHashMap<>();

    public InstallHandler(Class<?>... forClass) {
        if (forClass.length <= 0) throw new IllegalArgumentException("Class is missing");
        for (Class<?> c : forClass) {
            Installabel.handlers.put(c, this);
            Installabel.sHandlers.put(c.getName(), this);
        }

        this.forClass = forClass[0];
    }

    public void startGUI(InstallField f, String who) {
        this.context.put(who, f);
        this.toInstallField.put(f.field, f);
        start(f.field, who);
    }

    protected abstract void start(Field f, String who);

    public abstract String serialize(Object obj);

    public abstract Object deserilize(String d);

    public String serialize(Object obj, Field f) {
        return serialize(obj);
    }

    public Object deserilize(String d, Field f) {
        return deserilize(d);
    }

    public static class InstallField {
        public final Field field;
        public final Installabel instance;

        public InstallField(Field field, Installabel instance) {
            this.field = field;
            this.instance = instance;
        }

        public void set(Object value) {
            try {
                field.set(instance, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public Object get() {
            try {
                return field.get(instance);
            } catch (IllegalAccessException e) {
                return null;
            }

        }
    }

}
