package com.gameaurora.nova.modules.signcolors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.ChatColor;

public class SignColorListener implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (!event.getPlayer().hasPermission("nova.signcolors")) {
			return;
		}
		
		for (int i = 0; i < event.getLines().length; i++) {
			event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
		}
	}

}
