package com.gameaurora.nova.modules.pingchat;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.gameaurora.nova.Nova;

public class PingChatListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(final AsyncPlayerChatEvent event) {
		Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
			public void run() {
				for (final String word : event.getMessage().split(" ")) {
					for (Player player : event.getRecipients()) {
						if (word.contains(player.getName()) && (!player.getName().equalsIgnoreCase(event.getPlayer().getName()))) {
							ping(player);
						}
					}
				}
			}
		}, 5L);
	}

	private void ping(Player player) {
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 60, 1);
	}

}
