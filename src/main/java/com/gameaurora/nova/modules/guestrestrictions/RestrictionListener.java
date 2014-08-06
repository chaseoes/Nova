package com.gameaurora.nova.modules.guestrestrictions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gameaurora.milkyway.utilities.PermissionUtilities;
import com.gameaurora.nova.NovaMessages;

public class RestrictionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (PermissionUtilities.getPrimaryGroup(player).equalsIgnoreCase("default")) {
            if (event.getBlock().getType() == Material.TNT) {
                event.setBuild(false);
                event.setCancelled(true);
                notAllowed(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (PermissionUtilities.getPrimaryGroup(player).equalsIgnoreCase("default")) {
            if (player.getItemInHand() != null) {
                if (player.getItemInHand().getType().toString().endsWith("BUCKET") || player.getItemInHand().getType() == Material.FLINT_AND_STEEL) {
                    event.setCancelled(true);
                    event.setUseInteractedBlock(Result.DENY);
                    event.setUseItemInHand(Result.DENY);
                    notAllowed(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (PermissionUtilities.getPrimaryGroup(player).equalsIgnoreCase("default")) {
            if (event.getBlock().getType() == Material.DIAMOND_ORE) {
                event.setCancelled(true);
                notAllowed(player);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void notAllowed(Player player) {
        player.sendMessage(NovaMessages.PREFIX_ERROR + "You are not allowed to do that as a guest!");
        player.sendMessage(NovaMessages.PREFIX_GENERAL + "Rank up to " + ChatColor.BLUE + "[Member]" + ChatColor.GRAY + ": " + ChatColor.AQUA + "http://gameaurora.com/member");
        player.updateInventory();
    }

}
