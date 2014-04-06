package com.gameaurora.nova.modules.onlyproxyjoin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.gameaurora.nova.NovaMessages;

public class OnlyProxyJoinListener implements Listener {

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (!event.getAddress().getHostAddress().equals("127.0.0.1")) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, NovaMessages.PREFIX_ERROR + "Please connect through the proxy, sorry buddy!");
		}
	}

}
