package de.jonasfranz.Installable.bungee;


import de.jonasfranz.Installable.command.InstallCommandManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.LinkedHashMap;

public class BungeeCommandManager extends Command implements InstallCommandManager {


    public BungeeCommandManager() {
        super("binstall", "Installable.admin", new String[0]);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 1) {
            if (sender instanceof ProxiedPlayer) {
                //TODO Add Menu for BungeeCord
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
