package de.jonasfranz.Installable.bukkit;


import de.jonasfranz.Installable.command.InstallCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;

public class BukkitCommandManager implements InstallCommandManager, CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.isOp() && !sender.hasPermission("Installable.admin")) {
            sender.sendMessage("§cPermission denied.");
            return false;
        }
        if (args.length < 1) {
            if (sender instanceof Player) {
                ((Player) sender).openInventory(InventoryMenu.getMenu());
            } else {
                sender.sendMessage("Only a player can open an inventory.");
            }
        } else {
            if (this.args.containsKey(args[0])) {
                this.args.get(args[0]).execute(args, sender.getName());
            } else {
                sender.sendMessage("§c/install <args...>");
            }
        }
        return false;
    }

    private static final LinkedHashMap<String, InstallArgument> args = new LinkedHashMap<>();


    @Override
    public void registerArgument(String arg, InstallArgument iArg) {
        args.put(arg, iArg);
    }
}
