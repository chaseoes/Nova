package com.gameaurora.modules.bans;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.gameaurora.nova.NovaMessages;

public class BansListener implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (BanUtilities.getInstance().isBanned(event.getPlayer().getUniqueId())) {
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, NovaMessages.BANNED + BanUtilities.getInstance().getBanReason(event.getPlayer().getUniqueId()));
		}
	}
}
