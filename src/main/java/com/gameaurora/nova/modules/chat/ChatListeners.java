package com.gameaurora.nova.modules.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.StringUtil;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageUtilities;
import com.gameaurora.nova.modules.nametags.ServerScoreboard;
import com.gameaurora.nova.utilities.GeneralUtilities;
import com.gameaurora.nova.utilities.bungee.BungeeOnlinePlayerStorage;

public class ChatListeners implements Listener {

    public static HashMap<UUID, Long> tracker = new HashMap<UUID, Long>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(final AsyncPlayerChatEvent event) {

        final Player player = event.getPlayer();
        try {
            ConcurrentHashMap<String, ChatProfile> profiles = Nova.getInstance().chatData.profiles;
            if (profiles.containsKey(player.getName())) {
                ChatProfile profile = Nova.getInstance().chatData.profiles.get(player.getName());
                if (profile.isLoaded()) {
                    if (ChatUtilities.tooManyCaps(event.getMessage())) {
                        player.sendMessage(NovaMessages.PREFIX_ERROR + "That message has too many capital letters!");
                        event.setCancelled(true);
                        return;
                    }

                    for (String bannedWord : ChatUtilities.bannedWords) {
                        if (event.getMessage().toLowerCase().contains(bannedWord.toLowerCase())) {
                            player.sendMessage(NovaMessages.PREFIX_ERROR + "You are not allowed to say that, sorry! If you attempt to bypass this language censoring you will be banned.");
                            event.setCancelled(true);
                            return;
                        }
                    }

                    if ((event.getMessage().contains("http") || event.getMessage().contains("www.") || event.getMessage().contains(".com") || event.getMessage().contains(".net") || event.getMessage().contains(".org")) && !event.getMessage().contains("gameaurora") && !player.hasPermission("nova.url")) {
                        player.sendMessage(NovaMessages.PREFIX_ERROR + "You are not to post links!");
                        event.setCancelled(true);
                        return;
                    }

                    String ipPattern = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})";
                    Pattern r = Pattern.compile(ipPattern);
                    Matcher m = r.matcher(event.getMessage());
                    if (m.find()) {
                        player.sendMessage(NovaMessages.PREFIX_ERROR + "Advertising is not allowed! If you attempt to bypass this censoring then you will be banned.");
                        event.setCancelled(true);
                        return;
                    }

                    event.setFormat(profile.getChatFormat());
                    event.setCancelled(true);
                    ChatUtilities.buildFancyChatMessage(event.getFormat(), player.getName(), event.getMessage(), GeneralUtilities.getPrettyServerName()).send(event.getRecipients());
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

    @EventHandler
    public void onPlayerChatTabCompleteEvent(PlayerChatTabCompleteEvent event) {
        ArrayList<String> matches = StringUtil.copyPartialMatches(event.getChatMessage().substring(event.getChatMessage().lastIndexOf(" ") + 1), new ArrayList<String>(BungeeOnlinePlayerStorage.getOnlinePlayers()), new ArrayList<String>(BungeeOnlinePlayerStorage.getOnlinePlayers().size()));
        for (String p : matches) {
            if (!event.getTabCompletions().contains(p)) {
                event.getTabCompletions().add(p);
            }
        }
    }

    private String notReady() {
        return NovaMessages.PREFIX_ERROR + "Your chat profile hasn't loaded yet. If this error persists please contact a staff member.";
    }

}
