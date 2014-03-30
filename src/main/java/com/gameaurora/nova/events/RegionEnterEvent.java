package com.gameaurora.nova.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegionEnterEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String region;

    public RegionEnterEvent(Player player, String region) {
        this.player = player;
        this.region = region;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String getRegionName() {
        return region;
    }

}
