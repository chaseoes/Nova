package com.gameaurora.modules.bans;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanCommand implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("nova.ban")) {
			sender.sendMessage(NovaMessages.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + label + " <player> [proof]");
		} else {
			final String player = args[0];
			String reason = StringUtils.join(args, ' ', 1, args.length);
			if (reason.isEmpty()) {
				reason = "(none)";
			} else {
				if (!reason.startsWith("http://i.imgur.com/") && !reason.endsWith(".png")) {
					sender.sendMessage(NovaMessages.PREFIX_ERROR + "The proof must be a direct link to an image uploaded to http://imgur.com that ends in .png.");
					return true;
				}
			}
			final String finalReason = reason;
			sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Banning player " + ChatColor.GREEN + player + ChatColor.GRAY + " with proof " + ChatColor.GREEN + finalReason + ChatColor.GRAY + "..");
			if (Bukkit.getPlayer(player) != null) {
				Player p = Bukkit.getPlayer(player);
				UUID uuid = p.getUniqueId();
				BanUtilities.getInstance().banPlayer(uuid, reason, new BanSuccess(sender, player), new BanFail(sender, player));
				p.kickPlayer(NovaMessages.PREFIX_ERROR + "You have been banned!");
			} else {
				Bukkit.getScheduler().runTaskAsynchronously(Nova.getInstance(), new Runnable() {
					@Override
					public void run() {
						UUID uuid = null;
						try {
							uuid = UUIDFetcher.getUUIDOf(player);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (uuid == null) {
							Bukkit.getScheduler().runTask(Nova.getInstance(), new Runnable() {
								@Override
								public void run() {
									sender.sendMessage(NovaMessages.PREFIX_ERROR + "Could not lookup " + ChatColor.GREEN + player + ChatColor.RED + "'s UUID!");
								}
							});
						} else {
							BanUtilities.getInstance().banPlayer(uuid, finalReason, new BanSuccess(sender, player), new BanFail(sender, player));
						}
					}
				});
			}
		}
		return true;
	}

	private class BanSuccess implements Runnable {

		private CommandSender sender;
		private String player;

		private BanSuccess(CommandSender sender, String player) {
			this.sender = sender;
			this.player = player;
		}

		@Override
		public void run() {
			sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully banned player " + ChatColor.GREEN + player + ChatColor.GRAY + ".");
		}

	}

	private class BanFail implements Runnable {

		private CommandSender sender;
		private String player;

		private BanFail(CommandSender sender, String player) {
			this.sender = sender;
			this.player = player;
		}

		@Override
		public void run() {
			sender.sendMessage(NovaMessages.PREFIX_ERROR + "Failed to ban player " + ChatColor.GREEN + player + ChatColor.RED + ".");
		}

	}

}
