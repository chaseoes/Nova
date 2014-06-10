package com.gameaurora.nova.modules.locate;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.general.AuroraServer;
import com.gameaurora.nova.utilities.GeneralUtilities;
import com.gameaurora.nova.utilities.bungee.BungeeOnlinePlayerStorage;

public class LocateCommand implements CommandExecutor {

    // TODO: getPlayer() is deprecated. :(
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (strings.length == 0) {
            cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + string + " <player>");
            return true;
        }

        String player = strings[0];
        if (Nova.getInstance().getServer().getPlayer(player) != null) {
            player = Nova.getInstance().getServer().getPlayer(player).getName();
        }

        String currentServer = BungeeOnlinePlayerStorage.getCurrentServer(player);
        if (currentServer != null) {
            FancyMessage fm = new FancyMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + player + ChatColor.GRAY + " is on ").then(AuroraServer.valueOf(currentServer.toUpperCase()).getPrettyName()).color(ChatColor.AQUA);
            if (!GeneralUtilities.getServerName().equalsIgnoreCase(currentServer)) {
                fm = fm.tooltip(ChatColor.GRAY + "Click here to join their server!").command("/s " + currentServer.toLowerCase()).then(".").color(ChatColor.GRAY);
            } else {
                fm = fm.tooltip(ChatColor.GRAY + "That's the same server as you!").then(".").color(ChatColor.GRAY);
            }
            fm.send((Player) cs);
        } else {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + player + ChatColor.GRAY + " isn't online! :(");
        }
        return true;
    }
}
