package com.gameaurora.nova.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gameaurora.nova.modules.cloudmessages.CloudMessage;

public class CloudMessageReceiveEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private CloudMessage message;

    public CloudMessageReceiveEvent(CloudMessage message) {
        this.message = message;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CloudMessage getMessage() {
        return message;
    }

}
