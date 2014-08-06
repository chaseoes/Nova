package com.gameaurora.nova.modules.arrowtp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.AnimatedBallEntityEffect;

public class ArrowTeleportListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow) entity;
            ProjectileSource source = arrow.getShooter();
            if (source instanceof Player) {
                Player player = (Player) source;
                if (player.hasPermission("nova.arrowtp")) {
                    player.teleport(new Location(arrow.getWorld(), arrow.getLocation().getX(), arrow.getLocation().getY(), arrow.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                    player.playSound(arrow.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    player.getInventory().getItemInHand().setDurability((short) 0);
                    arrow.remove();

                    AnimatedBallEntityEffect ball = new AnimatedBallEntityEffect(Nova.getInstance().effectManager, player);
                    ball.size = 2;
                    ball.iterations = 10;
                    ball.start();
                } else {
                    player.sendMessage(NovaMessages.PREFIX_ERROR + "You must be a MVP or above to use the teleport bow!");
                    player.sendMessage(NovaMessages.PREFIX_ERROR + "Purchase it at: " + ChatColor.AQUA + "http://gameaurora.com/store");
                }
            }
        }
    }

}
