package com.gameaurora.nova.modules.cloudmessages;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.events.CloudMessageReceiveEvent;
import com.gameaurora.nova.modules.bans.BanUtilities;
import com.gameaurora.nova.modules.chat.ChatUtilities;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;
import com.gameaurora.nova.modules.privatemessages.MessageCommands;

public class CloudMessageListeners implements Listener {

    // TODO: getPlayer() is deprecated. :(
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

        if (message.getMessageType() == CloudMessageType.PRIVATE_MESSAGE) {
            String[] splitMessage = message.getMessage().split("\\|");
            Player player = Nova.getInstance().getServer().getPlayer(splitMessage[2]);
            if (player != null) {
                ChatUtilities.buildFancyChatMessage(ChatColor.GREEN + "(From %1$s)" + ChatColor.WHITE + ": %2$s", splitMessage[1], splitMessage[0], message.getSender().getPrettyName(), player).send(player);

                if (MessageCommands.lastMessaged.containsKey(player.getName())) {
                    MessageCommands.lastMessaged.remove(player.getName());
                }

                MessageCommands.lastMessaged.put(player.getName(), splitMessage[1]);
            }
        }

        if (message.getMessageType() == CloudMessageType.ALERT) {
            Nova.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
        }

        if (message.getMessageType() == CloudMessageType.PLAYER_COUNT) {
            PlayerCountUtilities.setPlayerCount(message.getSender().getName(), Integer.parseInt(message.getMessage()));
            return;
        }

        if (message.getMessageType() == CloudMessageType.KICK) {
            BanUtilities.getInstance().syncBans();

            final Player player = Nova.getInstance().getServer().getPlayer(message.getMessage());
            if (player != null) {
                Nova.getInstance().getServer().getScheduler().runTaskLater(Nova.getInstance(), new Runnable() {
                    public void run() {
                        player.kickPlayer(NovaMessages.KICKED);
                    }
                }, 80L);
            }
            return;
        }
    }
}
