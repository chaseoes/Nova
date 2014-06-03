package com.gameaurora.nova.modules.chat;

import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.PermissionUtilities;

// TODO: getPlayer is deprecated. :(
@SuppressWarnings("deprecation")
public class ChatProfile {

    String player;
    String chatFormat;
    String group;
    boolean loaded;

    public ChatProfile(Player player) {
        this.loaded = false;
        this.player = player.getName();
        load();
    }

    private void load() {
        group = PermissionUtilities.getGroupForPlayer(getPlayer());
        chatFormat = PermissionUtilities.getChatFormat(getPlayer());
        loaded = true;
    }

    public Player getPlayer() {
        return Nova.getInstance().getServer().getPlayerExact(player);
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public String getGroup() {
        return group;
    }

    public boolean isLoaded() {
        return loaded;
    }

}
