package com.gameaurora.nova.modules.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.hasBlock() && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.JUKEBOX || event.getClickedBlock().getState() instanceof InventoryHolder)) {
            Block b = event.getClickedBlock();
            if (b.getState() instanceof Sign) {
                Sign sign = (Sign) b.getState();
                if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Server Menu]")) {
                    player.performCommand("s");
                }
            }
            return;
        }

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().getItemInHand().getType() == Material.COMPASS || event.getPlayer().getItemInHand().getType() == Material.REDSTONE)) {
            if (event.getItem().hasItemMeta()) {
                if (event.getItem().getItemMeta().hasDisplayName()) {
                    if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals("Trail Selector")) {
                        player.performCommand("trail");
                        event.setUseInteractedBlock(Result.DENY);
                        event.setUseItemInHand(Result.DENY);
                        event.setCancelled(true);
                    } else if (ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equals("Server Menu")) {
                        player.performCommand("s");
                        event.setUseInteractedBlock(Result.DENY);
                        event.setUseItemInHand(Result.DENY);
                        event.setCancelled(true);
                    }
                }
            }

        }
    }

}
