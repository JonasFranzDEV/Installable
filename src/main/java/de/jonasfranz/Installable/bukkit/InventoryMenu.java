package de.jonasfranz.Installable.bukkit;


import de.jonasfranz.Installable.InstallHandler;
import de.jonasfranz.Installable.InstallManager;
import de.jonasfranz.Installable.Installabel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class InventoryMenu implements Listener {
    public static boolean isRegistered = false;

    public InventoryMenu() {
        isRegistered = true;
    }
    public static Inventory getMenu() {

        int inv_size = 9;
        while (InstallManager.items.size() > inv_size) {
            inv_size += 9;
        }
        Inventory inv = Bukkit.createInventory(null, inv_size, "#Hauptmenü");
        int item_nr = 0;
        for (String game : InstallManager.items.keySet()) {
            ItemStack s = new ItemStack(Material.GLASS);
            ItemMeta meta = s.getItemMeta();
            meta.setDisplayName(game);
            LinkedList<String> str = new LinkedList<>();
            str.add(InstallManager.getFields(InstallManager.items.get(game)).size() + " Settings");
            meta.setLore(str);
            s.setItemMeta(meta);
            inv.setItem(item_nr, s);
            item_nr++;
        }
        return inv;
    }

    public static Inventory getOptionMenu(String game) throws IllegalAccessException {
        int inv_size = 9;
        List<Field> fields = InstallManager.getFields(InstallManager.items.get(game));
        while (fields.size() > inv_size) {
            inv_size += 9;
        }
        Inventory inv = Bukkit.createInventory(null, inv_size, "#" + game);
        int nr = 0;
        for (Field f : fields) {
            ItemStack s = new ItemStack(Material.GLASS);
            ItemMeta meta = s.getItemMeta();
            meta.setDisplayName(f.getDeclaredAnnotation(Installabel.Install.class).name());
            LinkedList<String> str = new LinkedList<>();
            Object obj;
            if (Installabel.handlers.containsKey(f.getType())) {

                str.add("Value: " + ((obj = f.get(InstallManager.items.get(game))) != null ? Installabel.handlers.get(f.getType()).serialize(obj, f) : "-Not set-"));

                meta.setLore(str);
                s.setItemMeta(meta);
                inv.setItem(nr, s);
            } else {
                Bukkit.broadcastMessage("There is a problem. (like a bug)");
            }

            nr++;
        }
        return inv;
    }

    @EventHandler
    public void onHandle(InventoryClickEvent c) {
        if (!c.getWhoClicked().hasPermission("Installable.admin") && !c.getWhoClicked().isOp()) return;
        if (c.getInventory() != null && c.getInventory().getTitle() != null && c.getInventory().getTitle().startsWith("#")) {
            c.setCancelled(true);
            c.setResult(Event.Result.DENY);
            if (c.getCurrentItem() == null || c.getCurrentItem().getType() == null || !c.getCurrentItem().getType().equals(Material.GLASS)) {
                return;
            }
            c.getWhoClicked().closeInventory();
            switch (c.getInventory().getTitle().replace("#", "")) {
                case "Hauptmenü":
                    try {
                        c.getWhoClicked().openInventory(getOptionMenu(c.getCurrentItem().getItemMeta().getDisplayName()));
                    } catch (IllegalAccessException e) {
                        Bukkit.broadcastMessage("Ein Fehler ist aufgetreten:" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                default:
                    Field f = InstallManager.getField(c.getInventory().getTitle().replace("#", ""), c.getCurrentItem().getItemMeta().getDisplayName());
                    if (f == null) {

                        System.err.println("f == null");
                        break;
                    }
                    System.out.println(f.getName());

                    if (Installabel.handlers.containsKey(f.getType())) {
                        InstallHandler handler = Installabel.handlers.get(f.getType());

                        handler.startGUI(new InstallHandler.InstallField(f, InstallManager.items.get(c.getInventory().getTitle().replace("#", ""))), c.getWhoClicked().getName());
                    } else {
                        Bukkit.broadcastMessage("Es konnte keine Möglichkeit gefunden werden " + f.getType().getName() + " im Spiel zu setzen.");
                    }
                    break;
            }
        }
    }

}
