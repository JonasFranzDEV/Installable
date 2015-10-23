package de.jonasfranz.Installable;

import de.jonasfranz.Installable.types.*;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class InstallManager {
    @Deprecated
    public static LinkedHashMap<String, Installabel> items = new LinkedHashMap<String, Installabel>();
    public static LinkedHashMap<String, String> plugins = new LinkedHashMap<>();

    public static void registerInstallabel(Installabel i, String name, String plugin) {
        items.put(name, i);
        plugins.put(name, plugin);
    }
    

    public static Field getField(String key, String name) {
        for (Field f : getFields(items.get(key))) {
            if (f.getAnnotation(Installabel.Install.class).name().equalsIgnoreCase(name)) return f;

        }
        return null;
    }

    public static List<Field> getFields(Installabel i) {
        LinkedList<Field> fields = new LinkedList<Field>();
        for (Field f : i.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Installabel.Install.class)) {
                f.setAccessible(true);
                fields.add(f);
            }
        }
        Class parent = i.getClass();
        while ((parent = parent.getSuperclass()) != Object.class) {
            for (Field f : parent.getDeclaredFields()) {
                if (f.isAnnotationPresent(Installabel.Install.class)) {
                    f.setAccessible(true);
                    fields.add(f);
                }
            }
        }
        return fields;
    }

    private static boolean init = false;

    public static void initDefaultHandlers() {
        if (!init) {
            new SDouble();
            new SFloat();
            new SInteger();
            new SList();
            new SBoolean();
            new SString();
        }

    }


}
