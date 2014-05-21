package com.gameaurora.nova.modules.kick;

import org.bukkit.entity.Player;

import com.gameaurora.nova.NovaMessages;

public class KickUtilities {
	
	public static void kickPlayer(Player player) {
		player.kickPlayer(NovaMessages.KICKED);
	}

}
