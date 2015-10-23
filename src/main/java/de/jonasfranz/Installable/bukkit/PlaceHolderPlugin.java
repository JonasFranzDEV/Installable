package de.jonasfranz.Installable.bukkit;

import de.jonasfranz.Installable.bukkit.utils.BookGUI;
import org.bukkit.plugin.java.JavaPlugin;


public class PlaceHolderPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        if (!InventoryMenu.isRegistered) getServer().getPluginManager().registerEvents(new InventoryMenu(), this);
        if (!BookGUI.isRegistered) getServer().getPluginManager().registerEvents(new BookGUI(), this);
        getCommand("install").setExecutor(BukkitInstanceManager.cmdManager);
    }
}
