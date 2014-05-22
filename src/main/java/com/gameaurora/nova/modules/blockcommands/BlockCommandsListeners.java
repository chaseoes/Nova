package com.gameaurora.nova.modules.blockcommands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockCommandsListeners implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			BlockCommandsUtilities.runBlockCommand(event.getClickedBlock().getLocation(), event.getPlayer());
		}
	}

}
