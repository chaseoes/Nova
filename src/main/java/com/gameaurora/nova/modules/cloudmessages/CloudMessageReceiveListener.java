package com.gameaurora.nova.modules.cloudmessages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.events.CloudMessageReceiveEvent;

public class CloudMessageReceiveListener implements PluginMessageListener {

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("NovaCloudMessage")) {
                short len = in.readShort();
                byte[] msgbytes = new byte[len];
                in.readFully(msgbytes);
                DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

                // SHOULD BE RECEIVED IN THIS ORDER: message, message type, server name, pretty server name
                final CloudMessage cloudMessage = new CloudMessage(msgin.readUTF(), msgin.readUTF(), msgin.readUTF(), msgin.readUTF());

                Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
                    public void run() {
                        Nova.getInstance().getServer().getPluginManager().callEvent(new CloudMessageReceiveEvent(cloudMessage));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace(); // There was an error! D:
        }
    }

}
