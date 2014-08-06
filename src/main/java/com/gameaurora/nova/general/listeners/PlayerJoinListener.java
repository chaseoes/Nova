package com.gameaurora.nova.general.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.general.AuroraServer;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Set Online Player Count
        int playersOnline = Nova.getInstance().getServer().getOnlinePlayers().size();
        AuroraServer.valueOf(GeneralUtilities.getServerName().toUpperCase()).setPlayerCount(playersOnline);
    }

}
