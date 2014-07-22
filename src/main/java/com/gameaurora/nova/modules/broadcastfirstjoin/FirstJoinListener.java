package com.gameaurora.nova.modules.broadcastfirstjoin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.BungeeUtilities;

public class FirstJoinListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            BungeeUtilities.broadcastMessage(NovaMessages.FIRST_JOIN.replace("%player", event.getPlayer().getName()));
            Nova.getInstance().getServer().broadcastMessage(NovaMessages.FIRST_JOIN.replace("%player", event.getPlayer().getName()));
            return;
        }
    }

}
