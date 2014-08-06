package com.gameaurora.nova.general.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.milkyway.events.MilkyWayMessageEvent;
import com.gameaurora.milkyway.message.MilkyWayMessage;
import com.gameaurora.nova.general.AuroraServer;

public class MilkyWayMessageListener implements Listener {

    @EventHandler
    public void onMilkyWayMessage(MilkyWayMessageEvent event) {
        MilkyWayMessage message = event.getMessage();
        if (message.getSubchannel().equalsIgnoreCase("player-count")) {
            AuroraServer.valueOf(message.getMessages()[0].toUpperCase()).setPlayerCount(Integer.parseInt(message.getMessages()[1]));
        }
    }

}
