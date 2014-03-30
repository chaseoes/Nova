package com.gameaurora.nova.modules.chat;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class ChatListeners implements Listener {

	@EventHandler
	public void onAsyncChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		try {
			ConcurrentHashMap<String, ChatProfile> profiles = Nova.getInstance().chatData.profiles;
			if (profiles.containsKey(player.getName())) {
				ChatProfile profile = Nova.getInstance().chatData.profiles.get(player.getName());
				if (profile.isLoaded()) {
					event.setFormat(ChatColor.translateAlternateColorCodes('&', profile.getChatFormat().replace("<player>", "%1$s").replace("<message>", "%2$s")));
				} else {
					player.sendMessage(notReady());
					event.setCancelled(true);
					return;
				}
			} else {
				player.sendMessage(notReady());
				event.setCancelled(true);
				return;
			}
		} catch (Exception e) {
			event.setCancelled(true);
			e.printStackTrace();
			player.sendMessage(NovaMessages.PREFIX_ERROR + "An error occurred while handling your chat message.");
		}
	}
	
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChatProfile profile = new ChatProfile(event.getPlayer());
        Nova.getInstance().chatData.profiles.put(profile.getPlayer().getName(), profile);
    }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Nova.getInstance().chatData.profiles.remove(event.getPlayer().getName());
	}

	private String notReady() {
		return NovaMessages.PREFIX_ERROR + "Your chat profile hasn't loaded yet. If this error persists please contact a staff member.";
	}

}
