package com.gameaurora.nova.modules.launchpads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.AnimatedBallEntityEffect;

public class PadListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        Block plate = playerLoc.getWorld().getBlockAt(playerLoc);
        if (plate.getType() == Material.WOOD_PLATE || plate.getType() == Material.STONE_PLATE || plate.getType() == Material.GOLD_PLATE || plate.getType() == Material.IRON_PLATE) {
            double distance = PadUtilities.getLaunchDistance(plate.getLocation());
            if (distance != 0) {
                player.setVelocity(player.getLocation().getDirection().multiply(distance));
                player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
                for (Player p : Nova.getInstance().getServer().getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ARROW_HIT, 1.0F, 0.0F);
                }

                AnimatedBallEntityEffect ball = new AnimatedBallEntityEffect(Nova.getInstance().effectManager, player);
                ball.size = 2;
                ball.iterations = 10;
                ball.start();
            }
        }
    }

}
