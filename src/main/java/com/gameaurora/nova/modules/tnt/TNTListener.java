package com.gameaurora.nova.modules.tnt;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.gameaurora.nova.NovaMessages;

public class TNTListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("nova.tnt")) {
            event.setBuild(false);
            event.setCancelled(true);
            player.sendMessage(NovaMessages.PREFIX_ERROR + "You must be " + ChatColor.GREEN + "VIP+" + ChatColor.RED + " or above to use TNT!");
            player.sendMessage(NovaMessages.PREFIX_ERROR + "Buy a rank here: " + ChatColor.AQUA + "http://gameaurora.com/store");
        }
    }

}
