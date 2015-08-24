package de.jonasfranz.Installable.bukkit;

import de.jonasfranz.Installable.*;
import de.jonasfranz.Installable.bukkit.utils.BookGUI;
import de.jonasfranz.Installable.command.InstallCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;


public class BukkitInstallPlugin extends JavaPlugin implements InstallPlugin {


    @Override
    public void onEnable() {
        InstanceManager.instance = this;

        InstallManager.initDefaultHandlers();
        //Register the default command
        if (getCommand("install") != null && getServer().getPluginManager().getPlugin("Installable") == null)
            getCommand("install").setExecutor(BukkitInstanceManager.cmdManager);
        getServer().getPluginManager().registerEvents(new InventoryMenu(), this);
        if (!BookGUI.isRegistered) getServer().getPluginManager().registerEvents(new BookGUI(), this);
        //Restore saved values
        if (getConfig().getConfigurationSection("save") != null) {

            for (String key : InstallManager.items.keySet()) {
                if (getConfig().getConfigurationSection("save." + key) != null) {
                    Installabel i = InstallManager.items.get(key);
                    for (Field f : InstallManager.getFields(i)) {
                        if (getConfig().get("save." + key + "." + f.getName()) != null) {
                            InstallHandler handler = Installabel.handlers.get(f.getType());
                            if (handler != null) {
                                try {
                                    f.set(i, handler.deserilize(getConfig().getString("save." + key + "." + f.getName()), f));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                System.out.println(f.getName() + " loaded from Config.yml!");
                            }
                        }

                    }
                }
            }

        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        //Save values to config.yml
        for (String key : InstallManager.items.keySet()) {
            Installabel i = InstallManager.items.get(key);

            for (Field f : InstallManager.getFields(i)) {
                try {
                    Object obj = f.get(i);
                    getConfig().set("save." + key + "." + f.getName(), Installabel.handlers.get(f.getType()).serialize(obj, f));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    //Not set!
                }
            }
        }
        saveConfig();
        super.onDisable();
    }

    @Override
    public InstallCommandManager getCommandManager() {
        return BukkitInstanceManager.cmdManager;
    }

    @Override
    public void sendMessage(String to, String msg) {
        Bukkit.getPlayer(to).sendMessage(msg);
    }

    protected String install_command = "install";


    @Override
    public String getInstallCommand() {
        return install_command;
    }
}
