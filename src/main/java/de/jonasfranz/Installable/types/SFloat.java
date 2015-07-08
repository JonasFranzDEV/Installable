package de.jonasfranz.Installable.types;


import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.InstanceManager;
import de.jonasfranz.Installable.command.InstallCommandManager;

import java.lang.reflect.Field;


public class SFloat extends InstallHandler {

    public SFloat() {
        super(Float.class);
        InstallPlugin.instance.getCommandManager().registerArgument("float", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                f.set(Float.parseFloat(args[1]));
                InstallPlugin.instance.sendMessage(sender, "Saved.");
            }
        });
    }

    @Override
    protected void start(Field f, String who) {
        InstanceManager.instance.sendMessage(who, "Please set the value while enter /install float <value>");
    }

    @Override
    public String serialize(Object obj) {
        return Float.toString((Float) obj);
    }

    @Override
    public Object deserilize(String d) {
        return Float.parseFloat(d);
    }
}