package de.jonasfranz.Installable.types;


import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.InstanceManager;
import de.jonasfranz.Installable.command.InstallCommandManager;

import java.lang.reflect.Field;


public class SDouble extends InstallHandler {

    public SDouble() {
        super(Double.class);
        InstallPlugin.instance.getCommandManager().registerArgument("double", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                f.set(Double.parseDouble(args[1]));
                InstallPlugin.instance.sendMessage(sender, "Saved.");
            }
        });
    }

    @Override
    protected void start(Field f, String who) {
        InstanceManager.instance.sendMessage(who, "Please set the value while enter /install double <value>");
    }

    @Override
    public String serialize(Object obj) {
        return Double.toString((Double) obj);
    }

    @Override
    public Object deserilize(String d) {
        return Double.parseDouble(d);
    }
}