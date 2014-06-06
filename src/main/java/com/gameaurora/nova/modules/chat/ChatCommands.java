package com.gameaurora.nova.modules.chat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.BungeeUtilities;

public class ChatCommands implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("clearchat")) {
            if (cs.hasPermission("nova.clearchat")) {
                for (int i = 0; i <= 120; i++) {
                    Nova.getInstance().getServer().broadcastMessage(" ");
                    BungeeUtilities.broadcastMessage(" ");
                }

                Nova.getInstance().getServer().broadcastMessage(ChatColor.GRAY + "The chat was cleared by " + ChatColor.AQUA + cs.getName() + ChatColor.GRAY + ".");
                BungeeUtilities.broadcastMessage("&8The chat was cleared by &b" + cs.getName() + "&8.");
            } else {
                cs.sendMessage(NovaMessages.NO_PERMISSION);
            }
        }
        return true;
    }

}
