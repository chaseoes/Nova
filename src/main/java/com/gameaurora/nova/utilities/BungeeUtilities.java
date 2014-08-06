package com.gameaurora.nova.utilities;

import org.bukkit.entity.Player;

import com.gameaurora.milkyway.message.MilkyWayMessage;
import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeUtilities {

    public static void sendToServer(Player player, String server) {
        player.sendMessage(NovaMessages.CHANGE_SERVER);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Nova.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static void broadcastMessage(String message) {
        MilkyWayMessage milkyMessage = new MilkyWayMessage("alert", "raw", message);
        milkyMessage.send();
    }

}
