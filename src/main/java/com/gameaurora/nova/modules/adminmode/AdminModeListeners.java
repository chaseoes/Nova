package com.gameaurora.nova.modules.adminmode;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.ChatColor;
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
				if (event.getItem().getType() == Material.INK_SACK) {
					event.getPlayer().setResourcePack("http://emeraldsmc.com/downloads/resource_pack/resource_pack_1.7.4_256x.zip");
				}

				if (event.getItem().getType() == Material.WATCH) {
					event.getPlayer().performCommand("v");
				}

				if (event.getItem().getType() == Material.WOOD_HOE) {
					int r = new Random().nextInt(4);
					if (r == 0) {
						event.getPlayer().chat("Welcome!");
					}

					if (r == 1) {
						event.getPlayer().chat("Welcome! :D");	
					}

					if (r == 2) {
						event.getPlayer().chat("Welcome! :)");
					}

					if (r == 3) {
						event.getPlayer().chat("Hey, welcome to the server!");
					}

					if (r == 4) {
						event.getPlayer().chat("Welcome to the server!");
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (Nova.getInstance().adminPlayers.contains(event.getPlayer().getName())) {
			if (ChatColor.stripColor(event.getItemDrop().getItemStack().getItemMeta().getDisplayName()).startsWith("Admin")) {
				event.getItemDrop().remove();
				return;
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
