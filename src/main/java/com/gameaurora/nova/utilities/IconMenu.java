package com.gameaurora.nova.utilities;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class IconMenu implements Listener {

    private String name;
    private int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private Player player;
    private Inventory inventory;

    private String[] optionNames;
    private ItemStack[] optionIcons;

    public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        this.inventory = Bukkit.createInventory(player, size, name);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public void setSpecificTo(Player player) {
        this.player = player;
    }

    public boolean isSpecific() {
        return player != null;
    }

    public void reload() {
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
    }

    public void open(Player player) {
        reload();
        player.openInventory(inventory);
    }

    public int getSize() {
        return size;
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && (player == null || event.getWhoClicked() == player)) {
            event.setCancelled(true);
            if (event.getClick() != ClickType.LEFT)
                return;
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size && optionNames[slot] != null) {
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot], optionIcons[slot], event.getInventory());
                handler.onOptionClick(e);
                ((Player) event.getWhoClicked()).updateInventory();
                if (e.willClose()) {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            p.closeInventory();
                        }
                    });
                }
                if (e.willDestroy()) {
                    destroy();
                }
            }
        }
    }

    public interface OptionClickEventHandler {
        public void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {
        private Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean destroy;
        private ItemStack item;
        private Inventory inventory;

        public OptionClickEvent(Player player, int position, String name, ItemStack item, Inventory inventory) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
            this.item = item;
            this.inventory = inventory;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

        public ItemStack getItem() {
            return item;
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

}
