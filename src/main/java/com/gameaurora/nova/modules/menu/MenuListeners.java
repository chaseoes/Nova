package com.gameaurora.nova.modules.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class MenuListeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getItemInHand().getType() == Material.COMPASS) {
			if (event.hasBlock() && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.JUKEBOX || event.getClickedBlock().getState() instanceof InventoryHolder)) {
				return;
			}

			MenuUtilities.destroyCache(event.getPlayer());
			MenuUtilities.open(event.getPlayer());
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer() instanceof Player) {
			if (ChatColor.stripColor(event.getInventory().getName()).equals("Server Menu")) {
				MenuUtilities.destroyCache((Player) event.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		MenuUtilities.destroyCache(event.getPlayer());

		Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
			public void run() {
				for (String server : MenuUtilities.icons.keySet()) {
					GeneralUtilities.refreshPlayerCount(server);
				}

				Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
					public void run() {
						for (IconMenu menu : MenuUtilities.players.values()) {
							MenuUtilities.updateOptions(menu);
						}
					}
				}, 20L);
			}
		}, 40L);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().getInventory().clear();
		ItemStack i = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "Server Menu");
		i.setItemMeta(im);
		event.getPlayer().getInventory().addItem(i);
		
		
		MenuUtilities.destroyCache(event.getPlayer());

		Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
			public void run() {
				for (String server : MenuUtilities.icons.keySet()) {
					GeneralUtilities.refreshPlayerCount(server);
				}

				Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
					public void run() {
						for (IconMenu menu : MenuUtilities.players.values()) {
							MenuUtilities.updateOptions(menu);
						}
					}
				}, 20L);
			}
		}, 40L);
	}

}
