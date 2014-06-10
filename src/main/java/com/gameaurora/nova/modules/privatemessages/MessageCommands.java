package com.gameaurora.nova.modules.privatemessages;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.general.AuroraServer;
import com.gameaurora.nova.modules.chat.ChatUtilities;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageUtilities;
import com.gameaurora.nova.utilities.GeneralUtilities;
import com.gameaurora.nova.utilities.bungee.BungeeOnlinePlayerStorage;
import com.google.common.base.Joiner;

public class MessageCommands implements CommandExecutor {

    public static HashMap<String, String> lastMessaged = new HashMap<String, String>();

    // TODO: getPlayer is deprecated. :(
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("message")) {
            if (strings.length > 1) {
                Player to = Nova.getInstance().getServer().getPlayer(strings[0]);
                String toName = strings[0];
                String message = Joiner.on(" ").join(strings).replace(strings[0], "").trim();
                if (to != null || BungeeOnlinePlayerStorage.getOnlinePlayers().contains(toName)) {
                    if (to != null) {
                        toName = to.getName();
                    }

                    ChatUtilities.buildFancyChatMessage(ChatColor.GREEN + "(To %1$s)" + ChatColor.WHITE + ": %2$s", toName, message, AuroraServer.valueOf(BungeeOnlinePlayerStorage.getCurrentServer(toName).toUpperCase()).getPrettyName(), (Player) cs).send((Player) cs);
                    if (to != null) {
                        ChatUtilities.buildFancyChatMessage(ChatColor.GREEN + "(From %1$s)" + ChatColor.WHITE + ": %2$s", cs.getName(), message, GeneralUtilities.getPrettyServerName(), to).send(to);
                    } else {
                        CloudMessage cm = new CloudMessage(CloudMessageUtilities.createPrivateMessageString(message, cs.getName(), toName), CloudMessageType.PRIVATE_MESSAGE.toString(), GeneralUtilities.getServerName(), GeneralUtilities.getPrettyServerName());
                        cm.send();
                    }

                    for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
                        if (!(toName.equalsIgnoreCase(player.getName()) || cs.getName().equalsIgnoreCase(player.getName())) && player.isOp()) {
                            player.sendMessage(ChatColor.DARK_GRAY + "(" + cs.getName() + " -> " + toName + "): " + ChatColor.GRAY + message);
                        }
                    }

                    if (lastMessaged.containsKey(toName)) {
                        lastMessaged.remove(toName);
                    }

                    lastMessaged.put(toName, cs.getName());
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
