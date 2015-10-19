package de.jonasfranz.Installable.bukkit.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.LinkedHashMap;

public class BookGUI implements Listener {
    public static boolean isRegistered;

    public BookGUI() {
        isRegistered = true;
    }

    public interface ResultHandler {
        /**
         * This will be executed, after an user signed a GUI-Book.
         *
         * @return Is the String valid? If false then restart GUI.
         */
        public boolean handleResult(String result, Player player);
    }

    private static LinkedHashMap<Player, ResultHandler> players = new LinkedHashMap<>();
    private static LinkedHashMap<Player, ItemStack[]> invBackup = new LinkedHashMap<>();

    public static void startGUI(Player p, String description, String value, ResultHandler handler) {
        p.sendMessage("Please open the book in your inventory and follow the instructions.");
        value = value.replace("§", "&");
        if (!BookGUI.isRegistered)
            Bukkit.getPluginManager().registerEvents(new BookGUI(), Bukkit.getPluginManager().getPlugins()[0]);
        ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setDisplayName("§7Set: §6" + description);
        meta.setTitle(description);
        meta.setPages("Set the value of §6\"" + description.replace(':', ';') + "\"§r. Write the answer in this book and click on §6\"Sign\"§r:\n" + value);
        book.setItemMeta(meta);
        players.put(p, handler);
        invBackup.put(p, p.getInventory().getContents());
        p.getInventory().clear();
        p.getInventory().addItem(book);
    }

    @EventHandler
    public void onSign(PlayerEditBookEvent e) {
        if (e.isSigning() && players.containsKey(e.getPlayer())) {
            e.getPlayer().closeInventory();
            ResultHandler handler = players.get(e.getPlayer());
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setContents(invBackup.get(e.getPlayer()));
            Player p = e.getPlayer();
            String description = e.getPreviousBookMeta().getTitle();
            String value = ChatColor.stripColor(e.getNewBookMeta().getPage(1)).replace("\n", "").split(":")[1];

            if (!handler.handleResult(value, e.getPlayer())) {
                ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
                BookMeta meta = (BookMeta) book.getItemMeta();
                meta.setDisplayName("§7Set: §6" + description);
                meta.setTitle(description);
                meta.setPages("Set the value of §6\"" + description.replace(':', ';') + "\"§r. Write the answer in this book and click on §6\"Sign\"§r:\n" + value);
                book.setItemMeta(meta);
                players.put(p, handler);
                invBackup.put(p, p.getInventory().getContents());
                p.getInventory().clear();
                p.getInventory().addItem(book);
            } else {
                p.getInventory().clear();
                p.getInventory().setContents(invBackup.get(p));
                players.remove(p);
                invBackup.remove(p);
            }
        }
    }
}
