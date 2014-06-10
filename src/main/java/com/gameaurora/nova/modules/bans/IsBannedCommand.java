package com.gameaurora.nova.modules.bans;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

// TODO: getPlayer is deprecated. :(
@SuppressWarnings("deprecation")
public class IsBannedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("nova.isbanned")) {
            sender.sendMessage(NovaMessages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + label + " <player>");
        } else {
            final String player = args[0];
            if (Bukkit.getPlayer(player) != null) {
                sender.sendMessage(NovaMessages.PREFIX_GENERAL + "The player " + ChatColor.GREEN + player + ChatColor.GRAY + " is not banned.");
            } else {
                sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Checking bans for " + ChatColor.GREEN + player + ChatColor.GRAY + "..");
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
                            Bukkit.getScheduler().runTask(Nova.getInstance(), new CantFindPlayerCallback(sender, player));
                        } else if (BanUtilities.getInstance().isBanned(uuid)) {
                            Bukkit.getScheduler().runTask(Nova.getInstance(), new PlayerIsBannedCallback(sender, player));
                        } else {
                            Bukkit.getScheduler().runTask(Nova.getInstance(), new PlayerIsNotBannedCallback(sender, player));
                        }
                    }
                });
            }
        }

        return true;
    }

    private class CantFindPlayerCallback implements Runnable {

        private final CommandSender sender;
        private final String player;

        private CantFindPlayerCallback(CommandSender sender, String player) {
            this.sender = sender;
            this.player = player;
        }

        @Override
        public void run() {
            sender.sendMessage(NovaMessages.PREFIX_ERROR + "Could not lookup " + ChatColor.GREEN + player + ChatColor.RED + "'s UUID!");
        }

    }

    private class PlayerIsBannedCallback implements Runnable {

        private final CommandSender sender;
        private final String player;

        private PlayerIsBannedCallback(CommandSender sender, String player) {
            this.sender = sender;
            this.player = player;
        }

        @Override
        public void run() {
            sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Player " + ChatColor.GREEN + player + ChatColor.GRAY + " is banned.");
        }
    }

    private class PlayerIsNotBannedCallback implements Runnable {

        private final CommandSender sender;
        private final String player;

        private PlayerIsNotBannedCallback(CommandSender sender, String player) {
            this.sender = sender;
            this.player = player;
        }

        @Override
        public void run() {
            sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Player " + ChatColor.GREEN + player + ChatColor.GRAY + " is not banned.");
        }
    }

}
