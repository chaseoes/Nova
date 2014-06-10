package com.gameaurora.nova.modules.nametags;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gameaurora.nova.Nova;

public class ScoreboardListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Nova.getInstance().playerScoreboards.put(event.getPlayer().getName(), new ServerScoreboard(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Nova.getInstance().playerScoreboards.remove(event.getPlayer().getName());
    }

}
