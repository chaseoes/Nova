package com.gameaurora.nova.modules.cloudmessages;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.events.CloudMessageReceiveEvent;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;

public class CloudMessageListeners implements Listener {

    @EventHandler
    public void onCloudMessageReceive(CloudMessageReceiveEvent event) {
        CloudMessage message = event.getMessage();
        if (message.getMessageType() == CloudMessageType.CHAT) {
            FancyMessage fancyMessage = CloudMessageUtilities.splitUnformattedChatMessageString(message.getMessage(), message.getSender().getPrettyName());
            for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
                if (!player.hasMetadata("nova.cloudchat.disabled")) {
                    fancyMessage.send(player);
                }
            }
        }

        if (message.getMessageType() == CloudMessageType.PRIVATE_MESSAGE) {
            FancyMessage fancyMessage = CloudMessageUtilities.splitUnformattedChatMessageString(message.getMessage(), message.getSender().getPrettyName());
            for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
                if (!player.hasMetadata("nova.cloudchat.disabled")) {
                    fancyMessage.send(player);
                }
            }
        }

        if (message.getMessageType() == CloudMessageType.ALERT) {
            Nova.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
        }

        if (message.getMessageType() == CloudMessageType.PLAYER_COUNT) {
            PlayerCountUtilities.setPlayerCount(message.getSender().getName(), Integer.parseInt(message.getMessage()));
            return;
        }
    }
}
