package com.gameaurora.nova.modules.joinspawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class JoinSpawnListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		World lobby = Nova.getInstance().getServer().getWorld("lobby");
		Player player = event.getPlayer();

		if (lobby != null) {
			player.teleport(new Location(lobby, 0.5, 65, 0.5, -180, 0));
			GeneralUtilities.launchRandomFirework(player.getLocation());
		}
	}

}
