package com.gameaurora.nova.modules.cloudmessages;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.events.CloudMessageReceiveEvent;
import com.gameaurora.nova.modules.chat.ChatUtilities;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;

public class CloudMessageListeners implements Listener {

    // TODO: getPlayer is deprecated. :(
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onCloudMessageReceive(CloudMessageReceiveEvent event) {
        CloudMessage message = event.getMessage();
        if (message.getMessageType() == CloudMessageType.CHAT) {
            String[] splitMessage = message.getMessage().split("\\|");
            for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
                FancyMessage fm = ChatUtilities.buildFancyChatMessage(splitMessage[0], splitMessage[1], splitMessage[2], message.getSender().getPrettyName(), player);
                if (!player.hasMetadata("nova.cloudchat.disabled")) {
                    fm.send(player);
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

        if (message.getMessageType() == CloudMessageType.PLAYER_COMMAND) {
            String[] splitMessage = message.getMessage().split("\\|");
            Player targetPlayer = Nova.getInstance().getServer().getPlayer(splitMessage[0]);
            String command = splitMessage[1];

            if (targetPlayer != null) {
                targetPlayer.performCommand(command);
            }
        }

        if (message.getMessageType() == CloudMessageType.CONSOLE_COMMAND) {
            Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), message.getMessage());
        }
    }
}
