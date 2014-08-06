package com.gameaurora.nova.modules.adminmode;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gameaurora.nova.Nova;

public class AdminModeListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (Nova.getInstance().adminPlayers.contains(event.getPlayer().getName())) {
            event.getPlayer().performCommand("a");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Nova.getInstance().adminPlayers.contains(event.getPlayer().getName())) {
            if (event.hasItem()) {
                if (event.getItem().getType() == Material.WATCH) {
                    event.getPlayer().performCommand("v");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (Nova.getInstance().adminPlayers.contains(event.getPlayer().getName())) {
            if (event.getItemDrop().getItemStack().hasItemMeta()) {
                if (event.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) {
                    if (ChatColor.stripColor(event.getItemDrop().getItemStack().getItemMeta().getDisplayName()).startsWith("Admin")) {
                        event.getItemDrop().remove();
                        return;
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryMoveItem(InventoryClickEvent event) {
        if (Nova.getInstance().adminPlayers.contains(event.getWhoClicked().getName())) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().hasItemMeta()) {
                    if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
                        if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).startsWith("Admin")) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

}
