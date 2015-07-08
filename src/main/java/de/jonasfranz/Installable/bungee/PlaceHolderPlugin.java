package de.jonasfranz.Installable.bungee;


import net.md_5.bungee.api.plugin.Plugin;

public class PlaceHolderPlugin extends Plugin {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, BungeeInstanceManager.cmdManager);
    }
}
