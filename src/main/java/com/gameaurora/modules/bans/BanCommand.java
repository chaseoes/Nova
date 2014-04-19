package com.gameaurora.modules.bans;

import com.gameaurora.nova.Nova;
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
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <player> [reason]");
        } else {
            final String player = args[0];
            String reason = StringUtils.join(args, ' ', 1, args.length);
            if (reason.isEmpty()) {
                reason = "Banned!";
            }
            final String finalReason = reason;
            sender.sendMessage(ChatColor.GRAY + "Banning player " + ChatColor.GOLD + player + ChatColor.GRAY + " for reason " + ChatColor.GOLD + finalReason + ChatColor.GRAY + "...");
            if (Bukkit.getPlayer(player) != null) {
                Player p = Bukkit.getPlayer(player);
                UUID uuid = p.getUniqueId();
                BanUtilities.getInstance().banPlayer(uuid, reason, new BanSuccess(sender, player), new BanFail(sender, player));
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
                                    sender.sendMessage(ChatColor.RED + "Could not lookup " + ChatColor.GOLD + player + ChatColor.RED + "'s UUID!");
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
            sender.sendMessage(ChatColor.GRAY + "Successfully banned player " + ChatColor.GOLD + player + ChatColor.GRAY + ".");
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
            sender.sendMessage(ChatColor.RED + "Failed to ban player " + ChatColor.GOLD + player + ChatColor.RED + ".");
        }
    }
}
