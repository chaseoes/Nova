package com.gameaurora.nova.modules.chat;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageUtilities;
import com.gameaurora.nova.modules.nametags.ServerScoreboard;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class ChatListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(final AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();
        try {
            ConcurrentHashMap<String, ChatProfile> profiles = Nova.getInstance().chatData.profiles;
            if (profiles.containsKey(player.getName())) {
                ChatProfile profile = Nova.getInstance().chatData.profiles.get(player.getName());
                if (profile.isLoaded()) {
                    event.setFormat(profile.getChatFormat());
                } else {
                    Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
                        public void run() {
                            player.sendMessage(notReady());
                        }
                    });
                    event.setCancelled(true);
                    return;
                }
            } else {
                Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
                    public void run() {
                        player.sendMessage(notReady());
                    }
                });
                event.setCancelled(true);
                return;
            }
        } catch (Exception e) {
            event.setCancelled(true);
            e.printStackTrace();
            Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
                public void run() {
                    player.sendMessage(NovaMessages.PREFIX_ERROR + "An error occurred while handling your chat message.");
                }
            });
        }

        Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
            public void run() {
                if (Nova.getInstance().moduleIsEnabled("cloudmessages")) {
                    String unformattedMessage = CloudMessageUtilities.createChatMessageString(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
                    CloudMessage message = new CloudMessage(unformattedMessage, CloudMessageType.CHAT.toString(), GeneralUtilities.getServerName(), GeneralUtilities.getPrettyServerName());
                    message.send();
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChatProfile profile = new ChatProfile(event.getPlayer());
        Nova.getInstance().chatData.profiles.put(profile.getPlayer().getName(), profile);

        if (Nova.getInstance().moduleIsEnabled("nametags")) {
            ServerScoreboard board = new ServerScoreboard(event.getPlayer());
            board.updateBoard();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Nova.getInstance().chatData.profiles.remove(event.getPlayer().getName());
    }

    private String notReady() {
        return NovaMessages.PREFIX_ERROR + "Your chat profile hasn't loaded yet. If this error persists please contact a staff member.";
    }

}
