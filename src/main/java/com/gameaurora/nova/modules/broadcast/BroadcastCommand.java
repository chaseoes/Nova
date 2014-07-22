package com.gameaurora.nova.modules.broadcast;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class BroadcastCommand implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, final String[] strings) {
        if (cs.hasPermission("nova.broadcast")) {
            if (strings.length > 0) {
                Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
                    public void run() {
                        String message = StringUtils.join(strings, " ");
                        CloudMessage cloudMessage = new CloudMessage(NovaMessages.PREFIX_ALERT + message, CloudMessageType.ALERT.toString(), GeneralUtilities.getServerName(), GeneralUtilities.getPrettyServerName());
                        Nova.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', NovaMessages.PREFIX_ALERT) + message);
                        cloudMessage.send();
                    }
                }, 20L);
            } else {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + string + " <message>");
            }
        } else {
            cs.sendMessage(NovaMessages.NO_PERMISSION);
        }
        return true;
    }

}
