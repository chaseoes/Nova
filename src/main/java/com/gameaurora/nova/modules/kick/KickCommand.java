package com.gameaurora.nova.modules.kick;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class KickCommand implements CommandExecutor {

    // TODO: getPlayer is deprecated. :(
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (!cs.hasPermission("nova.kick")) {
            cs.sendMessage(NovaMessages.NO_PERMISSION);
            return true;
        }

        if (strings.length == 0) {
            cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + string + " <player>");
            return true;
        }

        Player targetPlayer = Nova.getInstance().getServer().getPlayer(strings[0]);
        if (targetPlayer == null) {
            cs.sendMessage(NovaMessages.PREFIX_ERROR + "That player isn't online.");
        } else {
            if (!targetPlayer.hasPermission("nova.kick")) {
                KickUtilities.kickPlayer(targetPlayer);
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + "The player " + ChatColor.GREEN + targetPlayer.getName() + ChatColor.GRAY + " was kicked successfully.");
            } else {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "You can't kick that player.");
            }
        }
        return true;
    }

}
