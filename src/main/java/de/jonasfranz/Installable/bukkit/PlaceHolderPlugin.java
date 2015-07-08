package de.jonasfranz.Installable.bukkit;

import org.bukkit.plugin.java.JavaPlugin;


public class PlaceHolderPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("install").setExecutor(BukkitInstanceManager.cmdManager);
    }
}
