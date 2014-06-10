package com.gameaurora.nova.modules.creativepass;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class CreativePassListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (GeneralUtilities.getServerName().equalsIgnoreCase("creative")) {
            if (!player.hasPermission("group.creativepass")) {
                event.setResult(Result.KICK_OTHER);
                event.setKickMessage(NovaMessages.PREFIX_GENERAL + "You must have a creative pass to join our creative server! Purchase one at: " + ChatColor.AQUA + "http://gameaurora.com/store");
            }
        }

        if (GeneralUtilities.getServerName().equalsIgnoreCase("buildteam")) {
            if (!player.hasPermission("nova.buildteam")) {
                event.setResult(Result.KICK_OTHER);
                event.setKickMessage(NovaMessages.PREFIX_GENERAL + "You must be a build team member to access that server.");
            }
        }

        if (GeneralUtilities.getServerName().equalsIgnoreCase("sandbox")) {
            if (!player.hasPermission("nova.sandbox")) {
                event.setResult(Result.KICK_OTHER);
                event.setKickMessage(NovaMessages.PREFIX_GENERAL + "You must be staff to access that server.");
            }
        }
    }

}
