package com.gameaurora.nova.modules.privatemessages;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.google.common.base.Joiner;

public class MessageCommands implements CommandExecutor {

    public static HashMap<String, String> lastMessaged = new HashMap<String, String>();

    // TODO: getPlayer is deprecated. :(
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("message")) {
            if (strings.length > 1) {
                Player to = Nova.getInstance().getServer().getPlayer(strings[0]);
                String message = Joiner.on(" ").join(strings).replace(strings[0], "").trim();
                if (to != null) {
                    cs.sendMessage(ChatColor.DARK_GRAY + "(To " + to.getDisplayName() + "): " + ChatColor.GRAY + message);
                    to.sendMessage(ChatColor.DARK_GRAY + "(From " + cs.getName() + "): " + ChatColor.GRAY + message);

                    for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
                        if (!(to.getName().equalsIgnoreCase(player.getName()) || cs.getName().equalsIgnoreCase(player.getName())) && player.isOp()) {
                            player.sendMessage(ChatColor.DARK_GRAY + "(" + cs.getName() + " -> " + to.getDisplayName() + "): " + ChatColor.GRAY + message);
                        }
                    }

                    if (lastMessaged.containsKey(to.getName())) {
                        lastMessaged.remove(to.getName());
                    }

                    lastMessaged.put(to.getName(), cs.getName());
                } else {
                    cs.sendMessage(NovaMessages.PREFIX_ERROR + "The player " + strings[0] + " is not online.");
                }
            } else {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /msg <player> <message>");
            }
        }

        if (cmnd.getName().equalsIgnoreCase("reply")) {
            if (strings.length > 0) {
                String message = Joiner.on(" ").join(strings);
                if (lastMessaged.containsKey(cs.getName())) {
                    ((Player) cs).performCommand("msg " + lastMessaged.get(cs.getName()) + " " + message);
                } else {
                    cs.sendMessage(NovaMessages.PREFIX_ERROR + "You haven't messaged anyone recently.");
                }
            } else {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /r <message>");
            }
        }
        return true;
    }

}
