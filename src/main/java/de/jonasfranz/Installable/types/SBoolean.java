package de.jonasfranz.Installable.types;


import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.Installabel;
import de.jonasfranz.Installable.InstanceManager;
import de.jonasfranz.Installable.bukkit.BukkitInstallPlugin;
import de.jonasfranz.Installable.bukkit.utils.BookGUI;
import de.jonasfranz.Installable.command.InstallCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;


public class SBoolean extends InstallHandler {

    public SBoolean() {
        super(Boolean.class, boolean.class);
        InstallPlugin.instance.getCommandManager().registerArgument("boolean", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false")) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease use /install boolean true/false");
                    return;
                }
                f.set(Boolean.parseBoolean((args[1])));
                InstallPlugin.instance.sendMessage(sender, "Saved.");
            }
        });
    }


    @Override
    protected void start(Field f, String who) {
        if (InstanceManager.instance instanceof BukkitInstallPlugin) {
            Object obj;
            final String sender = who;
            BookGUI.startGUI(Bukkit.getPlayer(who), f.getAnnotationsByType(Installabel.Install.class)[0].name(), ((obj = context.get(who).get()) != null ? Installabel.handlers.get(f.getType()).serialize(obj, f) : ""), new BookGUI.ResultHandler() {
                @Override
                public boolean handleResult(String result, Player player) {
                    result = result.replace(',', '.');
                    if (!result.equalsIgnoreCase("true") && !result.equalsIgnoreCase("false")) {
                        player.sendMessage("§cPlease enter true or false. " + result + " is not true or false.");
                        return false;
                    }
                    if (!context.containsKey(sender)) {
                        InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                        return false;
                    }
                    InstallField f = context.get(sender);
                    f.set(Boolean.parseBoolean(result));
                    InstallPlugin.instance.sendMessage(sender, "Saved.");
                    return true;
                }
            });
            return;
        }
        InstanceManager.instance.sendMessage(who, "Please set the value while enter /install boolean <true/false>");
    }

    @Override
    public String serialize(Object obj) {
        return Boolean.toString((Boolean) obj);
    }

    @Override
    public Object deserilize(String d) {
        return Boolean.parseBoolean(d);
    }
}