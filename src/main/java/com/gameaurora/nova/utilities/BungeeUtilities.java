package com.gameaurora.nova.utilities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.entity.Player;

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

	public static void forwardChatMessage(String toServer, String currentServerName, String playerName, String chatFormat, String chatMessage) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward");
		out.writeUTF(toServer);
		out.writeUTF("NovaChatMessage");

		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);

		try {
			msgout.writeUTF(currentServerName + Nova.CHANNEL_SEPARATOR);
			System.out.print(currentServerName);
			msgout.writeUTF(playerName + Nova.CHANNEL_SEPARATOR);
			System.out.print(playerName);
			msgout.writeUTF(chatFormat + Nova.CHANNEL_SEPARATOR);
			System.out.print(chatFormat);
			msgout.writeUTF(chatMessage);
			System.out.print(chatMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.writeShort(msgbytes.toByteArray().length);
		out.write(msgbytes.toByteArray());
		Nova.getInstance().getServer().sendPluginMessage(Nova.getInstance(), "BungeeCord", out.toByteArray());
	}

}
