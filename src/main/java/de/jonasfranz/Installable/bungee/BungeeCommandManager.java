package de.jonasfranz.Installable.bungee;


import de.jonasfranz.Installable.InstallManager;
import de.jonasfranz.Installable.Installabel;
import de.jonasfranz.Installable.command.InstallCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class BungeeCommandManager extends Command implements InstallCommandManager {


    public BungeeCommandManager() {
        super("binstall", "Installable.admin", new String[0]);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 1) {
            if (sender instanceof ProxiedPlayer) {
                List<TextComponent> comps = new LinkedList<>();
                for (String key : InstallManager.items.keySet()) {
                    TextComponent title = new TextComponent("::" + key);
                    title.setColor(ChatColor.GOLD);
                    title.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(InstallManager.getFields(InstallManager.items.get(key)).size() + " Settings")}));
                    comps.add(title);
                    for (Field f : InstallManager.getFields(InstallManager.items.get(key))) {
                        TextComponent subtile = new TextComponent("  > " + f.getDeclaredAnnotation(Installabel.Install.class).name());
                        subtile.setColor(ChatColor.GRAY);
                        Object obj;
                        if (Installabel.handlers.containsKey(f.getType())) try {
                            title.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Value: " + ((obj = f.get(InstallManager.items.get(key))) != null ? Installabel.handlers.get(f.getType()).serialize(obj, f) : "-Not set-"))}));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        subtile.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "bview " + key));
                        comps.add(subtile);
                    }

                }
                for (TextComponent c : comps) {
                    sender.sendMessage(c);
                }
            }
        } else {
            if (this.args.containsKey(args[0])) {
                this.args.get(args[0]).execute(args, sender.getName());
            } else {
                sender.sendMessage("§c/install <args...>");
            }
        }

    }


    private static final LinkedHashMap<String, InstallArgument> args = new LinkedHashMap<>();


    @Override
    public void registerArgument(String arg, InstallArgument iArg) {
        args.put(arg, iArg);
    }
}
