package de.jonasfranz.Installable.types;


import com.google.gson.Gson;
import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.Installabel;
import de.jonasfranz.Installable.InstanceManager;
import de.jonasfranz.Installable.command.InstallCommandManager;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public class SList extends InstallHandler {

    public SList() {
        super(List.class);
        InstallPlugin.instance.getCommandManager().registerArgument("list", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                f.set(lists.get(sender));
                InstallPlugin.instance.sendMessage(sender, "Saved.");
            }
        });
    }

    public static LinkedHashMap<String, List> lists = new LinkedHashMap<>();

    @Override
    protected void start(Field f, final String who) {
        InstanceManager.instance.sendMessage(who, "§lYou can add multiply values.");
        InstanceManager.instance.sendMessage(who, "§lIf you are done, please enter /install list.");
        Field stringListField = f;
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        final Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        System.out.println(stringListClass); // class java.lang.String.
        lists.put(who, new LinkedList());
        if (Installabel.handlers.containsKey(stringListClass)) {
            Installabel.handlers.get(stringListClass).startGUI(new InstallField(null, toInstallField.get(f).instance) {
                @Override
                public void set(Object value) {
                    lists.get(who).add(value);
                    Installabel.handlers.get(stringListClass).startGUI(this, who);
                }

                @Override
                public Object get() {
                    return null;
                }
            }, who);
        } else {
            InstanceManager.instance.sendMessage(who, "§cError: It was not found a matching handler.");
            return;
        }
    }

    @Deprecated
    public String serialize(Object obj) {
        return null;
    }

    @Deprecated
    public Object deserilize(String d) {
        return null;
    }

    @Override
    public String serialize(Object obj, Field f) {
        List<String> deList = new LinkedList<>();
        Field stringListField = f;
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        System.out.println(stringListClass); // class java.lang.String.
        if (Installabel.handlers.containsKey(stringListClass)) {
            InstallHandler h = Installabel.handlers.get(stringListClass);
            for (Object o : (List) obj) {
                deList.add(h.serialize(o));
            }
        } else {
            System.err.println("§cError: It could not found a matching handler.");
            return "[]";
        }
        String[] save = deList.toArray(new String[deList.size()]);
        return new Gson().toJson(save);
    }

    @Override
    public Object deserilize(String d, Field f) {
        List deList = new LinkedList();
        Field stringListField = f;
        ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
        Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
        System.out.println(stringListClass); // class java.lang.String.
        if (Installabel.handlers.containsKey(stringListClass)) {
            InstallHandler h = Installabel.handlers.get(stringListClass);
            for (String s : new Gson().fromJson(d, String[].class)) {
                deList.add(h.deserilize(s));
            }
        } else {
            System.err.println("Fehler: Es konnte kein Handler für " + stringListClass.getName() + " gefunden werden.");
            return "[]";
        }
        return deList;
    }
}