package com.gameaurora.nova.modules.teleportcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class TeleportRequest {

	private Player playerRequesting;
	private Player teleportToPlayer;

	public TeleportRequest(Player playerRequesting, Player teleportToPlayer) {
		this.playerRequesting = playerRequesting;
		this.teleportToPlayer = teleportToPlayer;
	}

	public void send() {
			teleportToPlayer.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + playerRequesting.getDisplayName() + ChatColor.GRAY + " has requested to teleport to you.");
			teleportToPlayer.sendMessage(NovaMessages.PREFIX_GENERAL + "Please type " + ChatColor.GREEN + "/tpa" + ChatColor.GRAY + " to accept.");
			
			final TeleportRequest request = this;
			Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
				public void run() {
					if (Nova.getInstance().teleportData.teleportRequests.contains(request)) {
						Nova.getInstance().teleportData.teleportRequests.remove(request);
						
						teleportToPlayer.sendMessage(NovaMessages.PREFIX_GENERAL + "The teleport request from " + ChatColor.GREEN + playerRequesting.getDisplayName() + ChatColor.GRAY + " has expired.");
						playerRequesting.sendMessage(NovaMessages.PREFIX_GENERAL + "Your teleport request to " + ChatColor.GREEN + teleportToPlayer.getDisplayName() + ChatColor.GRAY + " has expired.");
					}
				}
			}, 600L);
	}

	public Player getRequestingPlayer() {
		return playerRequesting;
	}

	public Player getPlayerToTeleportTo() {
		return teleportToPlayer;
	}

}
