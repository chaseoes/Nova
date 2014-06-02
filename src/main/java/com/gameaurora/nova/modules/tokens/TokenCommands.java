package com.gameaurora.nova.modules.tokens;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class TokenCommands implements CommandExecutor {

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("tokens")) {
            if (strings.length == 0) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                    return true;
                }
                Player player = (Player) cs;
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Your tokens: " + ChatColor.AQUA + TokenData.getTokens(player));
                return true;
            }

            if (strings[0].equalsIgnoreCase("add")) {
                if (cs.hasPermission("nova.tokens.add")) {
                    if (strings.length == 3) {
                        Player targetPlayer = Nova.getInstance().getServer().getPlayer(strings[1]);
                        if (targetPlayer != null) {
                            int amount = 0;

                            try {
                                amount = Integer.parseInt(strings[2]);
                            } catch (NumberFormatException e) {
                                cs.sendMessage(NovaMessages.PREFIX_ERROR + "That's not a number!");
                                return true;
                            }

                            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + targetPlayer.getName() + ChatColor.GRAY + " now has " + ChatColor.AQUA + TokenData.addTokens(targetPlayer, amount) + ChatColor.GRAY + " tokens.");
                        } else {
                            cs.sendMessage(NovaMessages.PREFIX_ERROR + "The player must be online!");
                        }
                    } else {
                        cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + string + " add <amount>");
                    }
                } else {
                    cs.sendMessage(NovaMessages.NO_PERMISSION);
                }
            }
        }
        return true;
    }
}
