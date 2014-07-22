package com.gameaurora.nova.modules.cloudmessages;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class CloudMessage {

    private String message;
    private CloudMessageType messageType;
    private CloudMessageSender sender;

    public CloudMessage(String message, String messageType, String senderName, String senderPrettyName) {
        this.message = message;
        this.messageType = CloudMessageType.valueOf(messageType.toUpperCase());
        this.sender = new CloudMessageSender(senderName, senderPrettyName);
    }

    public String getMessage() {
        return message;
    }

    public CloudMessageType getMessageType() {
        return messageType;
    }

    public CloudMessageSender getSender() {
        return sender;
    }

    public void send() {
        Nova.getInstance().getServer().getScheduler().runTask(Nova.getInstance(), new Runnable() {
            public void run() {
                try {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);

                    out.writeUTF("Forward"); // So bungeecord knows to forward it
                    out.writeUTF("ALL");
                    out.writeUTF("NovaCloudMessage"); // The channel name to check if this your data

                    ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
                    DataOutputStream msgout = new DataOutputStream(msgbytes);

                    // SHOULD BE SENT IN THIS ORDER: message, message type, server name, pretty server name
                    msgout.writeUTF(getMessage());
                    msgout.writeUTF(getMessageType().toString());
                    msgout.writeUTF(GeneralUtilities.getServerName());
                    msgout.writeUTF(GeneralUtilities.getPrettyServerName());

                    out.writeShort(msgbytes.toByteArray().length);
                    out.write(msgbytes.toByteArray());

                    try {
                        Player p = Nova.getInstance().getServer().getOnlinePlayers()[0];
                        p.sendPluginMessage(Nova.getInstance(), "BungeeCord", b.toByteArray());
                    } catch (Exception e) {

                    }
                } catch (Exception e) {
                    e.printStackTrace(); // OOPS! D:
                }
            }
        });
    }

}
