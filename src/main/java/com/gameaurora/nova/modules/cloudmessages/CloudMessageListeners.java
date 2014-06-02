package com.gameaurora.nova.modules.cloudmessages;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.nova.events.CloudMessageReceiveEvent;

public class CloudMessageListeners implements Listener {

    @EventHandler
    public void onCloudMessageReceive(CloudMessageReceiveEvent event) {
        CloudMessage message = event.getMessage();
        System.out.println("RECEIVED MESSAGE: " + message.getMessage());
        System.out.println("RECEIVED MESSAGE TYPE: " + message.getMessageType().toString());
        System.out.println("RECEIVED MESSAGE FROM SERVER: " + message.getSender().getName());
        System.out.println("RECEIVED MESSAGE FROM SERVER (PRETTY): " + message.getSender().getPrettyName());
    }

}
