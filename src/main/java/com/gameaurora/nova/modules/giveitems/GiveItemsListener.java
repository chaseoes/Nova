package com.gameaurora.nova.modules.giveitems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.utilities.GeneralUtilities;

public class GiveItemsListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GeneralUtilities.clearInventory(player);

        for (ItemType type : ItemType.values()) {
            if (type.isEnabled()) {
                type.add(player);
            }
        }
    }

}
