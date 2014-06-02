package com.gameaurora.nova.modules.tokens;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.modules.nametags.ServerScoreboard;

public class TokenListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
            ServerScoreboard board = new ServerScoreboard(player);
            board.updateBoard();
        }
    }

}
