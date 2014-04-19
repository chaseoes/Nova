package com.gameaurora.nova.modules.adminmode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gameaurora.nova.Nova;

public class AdminModeTask extends BukkitRunnable {

	public void run() {
		for (String s : Nova.getInstance().adminPlayers) {
			Player player = Nova.getInstance().getServer().getPlayer(s);
			if (player != null) {
				if (player.getGameMode() != GameMode.CREATIVE) {
					player.setGameMode(GameMode.CREATIVE);
				}

				player.setHealth(player.getMaxHealth());
			}
		}
	}

}
