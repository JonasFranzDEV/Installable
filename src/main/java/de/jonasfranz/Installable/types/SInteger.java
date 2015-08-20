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


public class SInteger extends InstallHandler {

    public SInteger() {
        super(Integer.class);
        InstallPlugin.instance.getCommandManager().registerArgument("integer", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                f.set(Integer.parseInt(args[1]));
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
                    try {
                        Integer i = Integer.parseInt(result);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§cPlease enter a number. " + result + " is not a number.");
                        return false;
                    }
                    if (!context.containsKey(sender)) {
                        InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                        return false;
                    }
                    InstallField f = context.get(sender);
                    f.set(Integer.parseInt(result));
                    InstallPlugin.instance.sendMessage(sender, "Saved.");
                    return true;
                }
            });
            return;
        }
        InstanceManager.instance.sendMessage(who, "Please set the value while enter /install integer <value>");
    }

    @Override
    public String serialize(Object obj) {
        return Integer.toString((Integer) obj);
    }

    @Override
    public Object deserilize(String d) {
        return Integer.parseInt(d);
    }
}