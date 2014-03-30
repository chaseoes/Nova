package com.gameaurora.nova.modules.permissions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PermissionListeners implements Listener {
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PermissionUtilities.getUtilities().setupPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (PermissionUtilities.getUtilities().getAttachment(event.getPlayer()) != null) {
            PermissionUtilities.getUtilities().getAttachment(event.getPlayer()).remove();
        }
    }
    
}
