package de.jonasfranz.Installable.bungee;

import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallManager;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.Installabel;
import de.jonasfranz.Installable.command.InstallCommandManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;


public class BungeeInstallPlugin extends Plugin implements InstallPlugin {
    private BungeeCommandManager cmdManager = new BungeeCommandManager();
    @Getter
    private Configuration config;

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, cmdManager);
        try {
            if (!getDataFolder().exists())
                getDataFolder().mkdir();

            File file = new File(getDataFolder(), "config.yml");

            if (!file.exists()) {
                file.createNewFile();
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            if (getConfig().getSection("save") != null) {

                for (String key : InstallManager.items.keySet()) {
                    if (getConfig().getSection("save." + key) != null) {
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
        } catch (IOException e) {
            //e.printStackTrace();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (config != null) {
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
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDisable();
    }

    @Override
    public InstallCommandManager getCommandManager() {
        return cmdManager;
    }

    @Override
    public void sendMessage(String to, String msg) {
        getProxy().getPlayer(to).sendMessage(msg);
    }

}
