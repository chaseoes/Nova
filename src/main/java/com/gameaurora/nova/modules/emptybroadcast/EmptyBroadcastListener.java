package com.gameaurora.nova.modules.emptybroadcast;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class EmptyBroadcastListener implements Listener {

    public static boolean broadcast = true;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (broadcast) {
            if (Nova.getInstance().getServer().getOnlinePlayers().length == 1) {
                String message = GeneralUtilities.getPrettyServerName() + " needs a couple more people! Come play? :D";
                CloudMessage cloudMessage = new CloudMessage(NovaMessages.PREFIX_ALERT + message, CloudMessageType.ALERT.toString(), GeneralUtilities.getServerName(), GeneralUtilities.getPrettyServerName());
                Nova.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', NovaMessages.PREFIX_ALERT) + message);
                cloudMessage.send();
            }

            broadcast = false;

            Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
                public void run() {
                    broadcast = true;
                }
            }, 600 * 20L);
        }
    }

}
