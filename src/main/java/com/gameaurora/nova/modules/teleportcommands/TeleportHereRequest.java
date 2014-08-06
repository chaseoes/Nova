package com.gameaurora.nova.modules.teleportcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class TeleportHereRequest {
	
	private Player playerRequesting;
	private Player teleportPlayerToPlayerRequesting;

	public TeleportHereRequest(Player playerRequesting, Player teleportPlayerToPlayerRequesting) {
		this.playerRequesting = playerRequesting;
		this.teleportPlayerToPlayerRequesting = teleportPlayerToPlayerRequesting;
	}

	public void send() {
		teleportPlayerToPlayerRequesting.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + playerRequesting.getDisplayName() + ChatColor.GRAY + " has requested to teleport you to them.");
		teleportPlayerToPlayerRequesting.sendMessage(NovaMessages.PREFIX_GENERAL + "Please type " + ChatColor.GREEN + "/tpa" + ChatColor.GRAY + " to accept.");
		
		final TeleportHereRequest request = this;
		Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
			public void run() {
				if (Nova.getInstance().teleportData.teleportHereRequests.contains(request)) {
					Nova.getInstance().teleportData.teleportHereRequests.remove(request);
					
					teleportPlayerToPlayerRequesting.sendMessage(NovaMessages.PREFIX_GENERAL + "The teleport request from " + ChatColor.GREEN + playerRequesting.getDisplayName() + ChatColor.GRAY + " has expired.");
					playerRequesting.sendMessage(NovaMessages.PREFIX_GENERAL + "Your teleport request to " + ChatColor.GREEN + teleportPlayerToPlayerRequesting.getDisplayName() + ChatColor.GRAY + " has expired.");
				}
			}
		}, 600L);
	}

	public Player getRequestingPlayer() {
		return playerRequesting;
	}

	public Player getPlayerToPlayerRequesting() {
		return teleportPlayerToPlayerRequesting;
	}

}
