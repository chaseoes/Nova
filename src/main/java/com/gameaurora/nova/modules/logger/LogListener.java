package com.gameaurora.nova.modules.logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

}
