package com.gameaurora.nova.modules.pads;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.gameaurora.nova.Nova;

public class PadListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location playerLoc = player.getLocation();
		Block plate = playerLoc.getWorld().getBlockAt(playerLoc);
		if (plate.getType() == Material.WOOD_PLATE || plate.getType() == Material.STONE_PLATE || plate.getType() == Material.GOLD_PLATE || plate.getType() == Material.IRON_PLATE) {
			if (!player.isSneaking()) {
				player.setVelocity(player.getLocation().getDirection().multiply(5.5));
				player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
			} else {
				player.setVelocity(player.getLocation().getDirection().multiply(8.5));
				player.setVelocity(new Vector(player.getVelocity().getX(), 1.5D, player.getVelocity().getZ()));
			}
			player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1.0F, 1.0F);
			for (Player all : Nova.getInstance().getServer().getOnlinePlayers()) {
				all.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
			}
		}
	}

}
