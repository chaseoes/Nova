package com.gameaurora.nova.modules.signlinks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gameaurora.nova.NovaMessages;

public class SignLinkListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock()) {
			Block b = event.getClickedBlock();
			if (b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN) {
				if (b.getState() instanceof Sign) {
					Sign sign = (Sign) b.getState();
					if (ChatColor.stripColor(sign.getLine(2)).equals("gameaurora")) {
						String url = "http://" + ChatColor.stripColor(sign.getLine(2)) + ChatColor.stripColor(sign.getLine(3));
						event.getPlayer().sendMessage(NovaMessages.PREFIX_GENERAL + "Click this URL:");
						event.getPlayer().sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + url);
					}
				}
			}
		}
	}

}
