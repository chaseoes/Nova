package com.gameaurora.nova.modules.spawnonjoin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;
import com.gameaurora.nova.utilities.SerializableLocation;

public class JoinSpawnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(SerializableLocation.stringToLocation(Nova.getInstance().getModule("spawn-on-join").getConfig().getString("location")));
        GeneralUtilities.launchRandomFirework(player.getLocation());
    }

}
