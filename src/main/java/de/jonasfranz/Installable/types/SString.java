package de.jonasfranz.Installable.types;


import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.command.InstallCommandManager;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;


public class SString extends InstallHandler {

    public SString() {
        super(String.class);
        InstallPlugin.instance.getCommandManager().registerArgument("string", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                f.set((args[1]));
                InstallPlugin.instance.sendMessage(sender, "Saved.");
            }
        });
    }

    @Override
    protected void start(Field f, Player who) {
        who.sendMessage("Please set the value while enter /install string <value>");
    }

    @Override
    public String serialize(Object obj) {
        return (String) obj;
    }

    @Override
    public Object deserilize(String d) {
        return d;
    }
}