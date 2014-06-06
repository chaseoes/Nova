package com.gameaurora.nova.modules.hubcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.BungeeUtilities;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class HubCommand implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
            return true;
        }

        final Player player = (Player) cs;
        if (strings != null && strings.length > 0) {
            if (strings[0].equalsIgnoreCase("-o")) {
                player.teleport(Nova.LOBBY_LOCATION);
            }
            return true;
        }

        String server = GeneralUtilities.getServerName();
        if (server.equalsIgnoreCase("survival")) {
            if (!string.startsWith("h")) {
                player.performCommand("mvspawn");
                return true;
            }
        }

        if (server.equalsIgnoreCase("skyblock")) {
            if (!string.startsWith("h")) {
                player.performCommand("lobby -o");
                return true;
            }
        }

        BungeeUtilities.sendToServer(player, "hub");
        return true;
    }

}
