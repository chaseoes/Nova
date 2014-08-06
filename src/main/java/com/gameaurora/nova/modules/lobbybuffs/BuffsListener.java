package com.gameaurora.nova.modules.lobbybuffs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BuffsListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (BuffType type : BuffType.values()) {
            if (type.isEnabled()) {
                type.apply(event.getPlayer());
            }
        }
    }

}
