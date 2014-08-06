package com.gameaurora.nova.modules.broadcast;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gameaurora.milkyway.message.MilkyWayMessage;
import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class BroadcastCommand implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, final String[] strings) {
        if (cs.hasPermission("nova.broadcast")) {
            if (strings.length > 0) {
                Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
                    public void run() {
                        String message = StringUtils.join(strings, " ");
                        MilkyWayMessage milkyMessage = new MilkyWayMessage("alert", "formatted", message);
                        milkyMessage.send();
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
