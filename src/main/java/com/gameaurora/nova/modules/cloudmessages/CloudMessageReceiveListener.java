package com.gameaurora.nova.modules.cloudmessages;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.events.CloudMessageReceiveEvent;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;

public class CloudMessageReceiveListener implements PluginMessageListener {

    public static List<byte[]> alreadyReceived = new ArrayList<byte[]>();

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!alreadyReceived.contains(message)) {
            if (!channel.equals("BungeeCord")) {
                return;
            }

            if (Nova.getInstance().getServer().getOnlinePlayers().length == 0) {
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

                if (subchannel.equals("PlayerCount")) {
                    try {
                        String server = in.readUTF();
                        int playerCount = in.readInt();
                        PlayerCountUtilities.setPlayerCount(server.toLowerCase().trim(), playerCount);
                    } catch (Exception e) {
                        // What the fuck?
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // There was an error! D:
            }

            alreadyReceived.add(message);
        }
    }
}
