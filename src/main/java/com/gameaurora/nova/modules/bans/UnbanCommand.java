package com.gameaurora.nova.modules.bans;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

// TODO: getPlayer is deprecated. :(
@SuppressWarnings("deprecation")
public class UnbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("nova.unban")) {
            sender.sendMessage(NovaMessages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <player>");
        } else {
            final String player = args[0];
            sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Unbanning player " + ChatColor.GREEN + player + ChatColor.GRAY + "..");
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
                                    sender.sendMessage(NovaMessages.PREFIX_ERROR + "Could not lookup " + ChatColor.GREEN + player + ChatColor.RED + "'s UUID!");
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
            sender.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully unbanned player " + ChatColor.GREEN + player + ChatColor.GRAY + ".");
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
            sender.sendMessage(NovaMessages.PREFIX_ERROR + "Failed to unban player " + ChatColor.GREEN + player + ChatColor.RED + ".");
        }
    }

}
