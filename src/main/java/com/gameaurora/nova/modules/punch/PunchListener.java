package com.gameaurora.nova.modules.punch;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class PunchListener implements Listener {

	public static List<String> cantUse = new ArrayList<String>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onPlayerHitEvent(EntityDamageByEntityEvent event) {
		Entity attacker = event.getDamager();
		if (event.getEntity().getWorld().getName().equals("lobby") && attacker instanceof Arrow) {
			attacker.remove();
			event.setCancelled(true);
		}
		
		if(attacker instanceof Player && event.getEntity() instanceof Player) {
			Player player = (Player) attacker;
			if (player.getLocation().getWorld().getName().equals("lobby")) {
				if (player.hasPermission("nova.launch")) {
					if (((Player) event.getEntity()).hasPermission("nova.launchable")) {
						if (!cantUse.contains(player.getName())) {
							Nova.getInstance().getServer().broadcastMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + player.getName() + ChatColor.GRAY + " punched " + ChatColor.GREEN + ((Player) event.getEntity()).getName() + ChatColor.GRAY + " into the air!");
							for (Player onlinePlayer : Nova.getInstance().getServer().getOnlinePlayers()) {
								onlinePlayer.playSound(event.getEntity().getLocation(), Sound.EXPLODE, 1, 1);
							}

							event.getEntity().setVelocity(new Vector(event.getEntity().getVelocity().getX(), 7.0D, event.getEntity().getVelocity().getZ()));

							final String playerName = player.getName();
							cantUse.add(playerName);

							Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
								public void run() {
									cantUse.remove(playerName);

								}
							}, 6000L);
						} else {
							player.sendMessage(NovaMessages.PREFIX_ERROR + "You must wait 5 minutes before using that again!");
						}
					}
				}
				event.setCancelled(true);
			}
		}
	}

}
