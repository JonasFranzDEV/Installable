package de.jonasfranz.Installable.types;

import com.google.gson.Gson;
import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallPlugin;
import de.jonasfranz.Installable.InstanceManager;
import de.jonasfranz.Installable.command.InstallCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Field;


public class SLocation extends InstallHandler {
    public static class Holder {
        private double x, y, z;
        private String world;

        public Location toLocation() {
            World w = Bukkit.getWorld(world);
            if (w == null)
                return null;
            Location toRet = new Location(w, x, y, z);
            return toRet;
        }

        public Holder fromLocation(Location loc) {
            x = loc.getX();
            y = loc.getY();
            z = loc.getZ();
            world = loc.getWorld().getName();
            return this;
        }
    }


    public SLocation() {
        super(Location.class);
        InstallPlugin.instance.getCommandManager().registerArgument("location", new InstallCommandManager.InstallArgument() {
            @Override
            public void execute(String[] args, String sender) {
                if (!context.containsKey(sender)) {
                    InstallPlugin.instance.sendMessage(sender, "§cPlease first choose an item in the /install-Panel.");
                    return;
                }
                InstallField f = context.get(sender);
                f.set(Bukkit.getPlayer(sender).getLocation());
                InstallPlugin.instance.sendMessage(sender, "Saved.");
            }
        });
    }


    @Override
    public void start(Field f, String who) {
        InstanceManager.instance.sendMessage(who, "Go to the location and enter /install location");

    }

    @Override
    public String serialize(Object object) {
        Holder h = new Holder();
        h.fromLocation((Location) object);
        return new Gson().toJson(h);
    }

    @Override
    public Object deserilize(String d) {
        Holder h = new Gson().fromJson(d, Holder.class);
        return h.toLocation();
    }

}
