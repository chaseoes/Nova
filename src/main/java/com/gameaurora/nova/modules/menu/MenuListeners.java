package com.gameaurora.nova.modules.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
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

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class MenuListeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.hasBlock() && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.JUKEBOX || event.getClickedBlock().getState() instanceof InventoryHolder)) {
			Block b = event.getClickedBlock();
			if (b.getState() instanceof Sign) {
				Sign sign = (Sign) b.getState();
				if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Server Menu]")) {
					player.performCommand("s");
				}
			}
			return;
		}

		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.COMPASS || event.getPlayer().getItemInHand().getType() == Material.REDSTONE)) {
			if (event.getItem().hasItemMeta()) {
				if (event.getItem().getItemMeta().hasDisplayName()) {
					if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals("Trail Selector")) {
						player.performCommand("trail");
					} else if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals("Server Menu")) {						
						player.performCommand("s");
					}
					event.setUseInteractedBlock(Result.DENY);
					event.setUseItemInHand(Result.DENY);
					event.setCancelled(true);
				}
			}

		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (Nova.getInstance().moduleIsEnabled("menuitem")) {
			player.getInventory().clear(); 
			player.getInventory().addItem(GeneralUtilities.getCustomItem(Material.COMPASS, ChatColor.AQUA + "Server Menu", ChatColor.GRAY + "Right click to open."));
		}

		if (Nova.getInstance().moduleIsEnabled("trailselector")) {
			player.getInventory().addItem(GeneralUtilities.getCustomItem(Material.REDSTONE, ChatColor.GREEN + "Trail Selector", ChatColor.GRAY + "Right click to open."));
		}

		if (Nova.getInstance().moduleIsEnabled("arrowtp")) {
			ItemStack bow = GeneralUtilities.getCustomItem(Material.BOW, ChatColor.GREEN + "Teleport Bow", ChatColor.GRAY + "Shoot to teleport!");
			bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			player.getInventory().addItem(bow);
			player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
		}

		MenuUtilities.destroyCache(player);

		Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
			public void run() {
				for (String server : MenuUtilities.icons.keySet()) {
					PlayerCountUtilities.requestPlayerCount(server);
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
	public void onPlayerQuit(PlayerQuitEvent event) {
		MenuUtilities.destroyCache(event.getPlayer());

		Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
			public void run() {
				for (String server : MenuUtilities.icons.keySet()) {
					PlayerCountUtilities.requestPlayerCount(server);
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer() instanceof Player) {
			if (ChatColor.stripColor(event.getInventory().getName()).equals("Server Menu")) {
				MenuUtilities.destroyCache((Player) event.getPlayer());
			}
		}
	}

}
