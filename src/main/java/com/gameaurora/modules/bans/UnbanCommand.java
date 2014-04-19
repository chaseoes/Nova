package com.gameaurora.modules.bans;

import com.gameaurora.nova.Nova;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("nova.unban")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <player>");
        } else {
            final String player = args[0];
            sender.sendMessage(ChatColor.GRAY + "Unbanning player " + ChatColor.GOLD + player + ChatColor.GRAY + "...");
            if (Bukkit.getPlayer(player) != null) {
                Player p = Bukkit.getPlayer(player);
                UUID uuid = p.getUniqueId();
                BanUtilities.getInstance().unbanPlayer(uuid, new UnbanSuccess(sender, player), new UnbanFail(sender, player));
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
                            BanUtilities.getInstance().unbanPlayer(uuid, new UnbanSuccess(sender, player), new UnbanFail(sender, player));
                        }
                    }
                });
            }
        }
        return true;
    }

    private class UnbanSuccess implements Runnable {

        private final CommandSender sender;
        private final String player;

        public UnbanSuccess(CommandSender sender, String player) {
            this.sender = sender;
            this.player = player;
        }

        @Override
        public void run() {
            sender.sendMessage(ChatColor.GRAY + "Successfully unbanned player " + ChatColor.GOLD + player + ChatColor.GRAY + ".");
        }
    }

    private class UnbanFail implements Runnable {

        private final CommandSender sender;
        private final String player;

        public UnbanFail(CommandSender sender, String player) {
            this.sender = sender;
            this.player = player;
        }

        @Override
        public void run() {
            sender.sendMessage(ChatColor.RED + "Failed to unban player " + ChatColor.GOLD + player + ChatColor.RED + ".");
        }
    }
}
