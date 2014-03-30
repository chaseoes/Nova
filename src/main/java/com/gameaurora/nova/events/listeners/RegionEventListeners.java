package com.gameaurora.nova.events.listeners;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.events.RegionEnterEvent;
import com.gameaurora.nova.events.RegionExitEvent;
import com.gameaurora.nova.utilities.RegionUtilities;

public class RegionEventListeners implements Listener {
	
	HashMap<String, String> inRegion = new HashMap<String, String>();
	
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String region = RegionUtilities.getRegion(event.getTo());
        
        if (inRegion.containsKey(player.getName())) {
            String lastIn = inRegion.get(player.getName());
            if (region != null) {
                if (!lastIn.equals(region)) {
                	Nova.getInstance().getServer().getPluginManager().callEvent(new RegionEnterEvent(player, region));
                	Nova.getInstance().getServer().getPluginManager().callEvent(new RegionExitEvent(player, lastIn));
                    inRegion.remove(player.getName());
                    inRegion.put(player.getName(), region);
                }
            } else {
            	Nova.getInstance().getServer().getPluginManager().callEvent(new RegionExitEvent(player, lastIn));
                inRegion.remove(player.getName());
            }
        } else {
            if (region != null) {
            	Nova.getInstance().getServer().getPluginManager().callEvent(new RegionEnterEvent(player, region));
                inRegion.put(player.getName(), region);
            }
        }
    }
	
}
