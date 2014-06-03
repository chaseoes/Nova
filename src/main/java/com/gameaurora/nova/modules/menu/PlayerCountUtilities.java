package com.gameaurora.nova.modules.menu;

import java.util.HashMap;

import com.gameaurora.nova.Nova;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class PlayerCountUtilities {

    private static HashMap<String, Integer> playerCounts = new HashMap<String, Integer>();

    public static int getPlayerCount(String s) {
        String server = convertServerName(s);
        if (playerCounts.containsKey(server)) {
            return playerCounts.get(server);
        }
        return -1;
    }

    public static void setPlayerCount(String server, int i) {
        playerCounts.remove(server);
        playerCounts.put(server, i);
    }

    public static void requestPlayerCount(String s) {
        String server = convertServerName(s);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        Nova.getInstance().getServer().sendPluginMessage(Nova.getInstance(), "BungeeCord", out.toByteArray());
    }

    private static String convertServerName(String server) {
        return server.replace(" ", "").toLowerCase();
    }

}
