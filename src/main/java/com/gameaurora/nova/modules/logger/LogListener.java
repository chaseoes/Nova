package com.gameaurora.nova.modules.logger;

import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gameaurora.nova.Nova;

public class LogListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerAsyncChat(AsyncPlayerChatEvent event) {
		final String name = event.getPlayer().getName();
		final String message = event.getMessage();
		Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
			public void run() {
				LoggerUtilities.log("[" + name + "] " + message, LogEntryType.CHAT);
			}
		});
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		LoggerUtilities.log("[" + event.getPlayer().getName() + "] " + event.getMessage(), LogEntryType.COMMAND);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		if (Nova.getInstance().moduleIsEnabled("loghoppers")) {
			if (event.getInitiator().getHolder() instanceof Hopper) {
				Hopper hopper = (Hopper) event.getInitiator().getHolder();
				Location hopperLocation = hopper.getLocation();
				LoggerUtilities.log(hopperLocation.getBlockX() + " " + hopperLocation.getBlockY() + " " + hopperLocation.getBlockZ() + " " + event.isCancelled(), LogEntryType.HOPPER);
			}
		}
	}

}
