package com.gameaurora.nova.utilities;

import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
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
        new CloudMessage(message, CloudMessageType.ALERT.toString(), GeneralUtilities.getServerName(), GeneralUtilities.getPrettyServerName()).send();
    }

}
