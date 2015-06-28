package de.jonasfranz.Installable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;

/**
 * Diese Datei unterliegt dem Urheberrecht und darf nur mit der Genehmigung des Autors verwendet, bearbeitet, veröffentlicht oder weitergegeben werden.
 * Autor des Quelltexts: ${user}
 * Sollten Zeilen bearbeitet oder hinzugefügt werden, muss dies per Kommentar gekennzeichnet werden.
 * Dieser Hinweis darf NICHT ENTFERNT werden!
 * Erstelldatum: ${date}
 * Projekt: Arcade
 */
public interface Installabel {
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Install {

        public String name();

        public String plugin();
    }

    public static class DataHolder {
        public String type;
        public String value;
    }

    public static LinkedHashMap<Class<?>, InstallHandler> handlers = new LinkedHashMap<>();
    public static LinkedHashMap<String, InstallHandler> sHandlers = new LinkedHashMap<>();
}
