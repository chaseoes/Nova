package com.gameaurora.nova.modules.maintenancemode;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class MaintenanceModeTask extends BukkitRunnable {

	public void run() {
		Nova.getInstance().getServer().broadcastMessage(NovaMessages.PREFIX_ERROR + "This server is currently in " + ChatColor.BOLD + "MAINTENANCE MODE" + ChatColor.RED + ".");
		Nova.getInstance().getServer().broadcastMessage(NovaMessages.PREFIX_ERROR + "This means there may be bugs, lag, disconnects, or crashes. Please be patient while we improve the network.");
	}

}
