package com.gameaurora.nova.modules.chat;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;

public class ChatData {
	
	public ConcurrentHashMap<String, ChatProfile> profiles = new ConcurrentHashMap<String, ChatProfile>();
	
	public void reloadProfiles() {
		profiles.clear();
		for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
			ChatProfile profile = new ChatProfile(player);
			profiles.put(profile.getPlayer().getName(), profile);
		}
	}

}
