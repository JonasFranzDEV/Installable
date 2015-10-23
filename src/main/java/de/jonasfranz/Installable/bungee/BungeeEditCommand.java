package de.jonasfranz.Installable.bungee;

import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallManager;
import de.jonasfranz.Installable.Installabel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Field;
import java.util.Base64;

public class BungeeEditCommand extends Command {
    public BungeeEditCommand() {
        super("bview", "Installable.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length < 2) {
            BungeeInstanceManager.cmdManager.execute(commandSender, args);
            return;
        }
        Field f = InstallManager.getField(new String(Base64.getDecoder().decode(args[0])), new String(Base64.getDecoder().decode(args[1])));
        if (f == null) {
            BungeeInstanceManager.cmdManager.execute(commandSender, args);
            return;
        }
        if (Installabel.handlers.containsKey(f.getType())) {
            InstallHandler handler = Installabel.handlers.get(f.getType());

            handler.startGUI(new InstallHandler.InstallField(f, InstallManager.items.get(new String(Base64.getDecoder().decode(args[0])))), commandSender.getName());
        } else {
            commandSender.sendMessage("Es konnte keine Möglichkeit gefunden werden " + f.getType().getName() + " im Spiel zu setzen.");
        }
    }
}
