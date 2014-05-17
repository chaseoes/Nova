package com.gameaurora.nova.modules.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.inventory.meta.ItemMeta;

import com.gameaurora.nova.Nova;

public class MenuListeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.COMPASS || event.getPlayer().getItemInHand().getType() == Material.REDSTONE)) {
			if (event.hasBlock() && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.JUKEBOX || event.getClickedBlock().getState() instanceof InventoryHolder)) {
				return;
			}

			if (event.getItem().hasItemMeta()) {
				if (event.getItem().getItemMeta().hasDisplayName()) {
					if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals("Trail Selector")) {
						event.getPlayer().performCommand("trail");
					} else if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals("Server Menu")) {						
						MenuUtilities.destroyCache(event.getPlayer());
						MenuUtilities.open(event.getPlayer());
					}
					event.setUseInteractedBlock(Result.DENY);
					event.setUseItemInHand(Result.DENY);
					event.setCancelled(true);
				}
			}

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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().getInventory().clear();
		ItemStack i = new ItemStack(Material.COMPASS, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.AQUA + "Server Menu");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Right click to open.");
		im.setLore(lore);
		i.setItemMeta(im);
		event.getPlayer().getInventory().addItem(i);

		if (Nova.getInstance().moduleIsEnabled("trailselector")) {
			i = new ItemStack(Material.REDSTONE, 1);
			im = i.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Trail Selector");
			List<String> trailLore = new ArrayList<String>();
			trailLore.add(ChatColor.GRAY + "Right click to open.");
			im.setLore(trailLore);
			i.setItemMeta(im);
			event.getPlayer().getInventory().addItem(i);
		}

		if (Nova.getInstance().moduleIsEnabled("arrowtp")) {
			i = new ItemStack(Material.BOW, 1);
			im = i.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Teleport Bow");
			List<String> arrowLore = new ArrayList<String>();
			arrowLore.add(ChatColor.GRAY + "Shoot to teleport!");
			im.setLore(arrowLore);
			i.setItemMeta(im);
			i.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			event.getPlayer().getInventory().addItem(i);
			event.getPlayer().getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
		}

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

}
