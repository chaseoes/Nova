package com.gameaurora.nova.utilities.bungee;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.general.AuroraServer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeOnlinePlayerStorage {

    private static HashMap<String, String> allPlayersOnline = new HashMap<String, String>(); // playerName, serverName

    /**
     * Returns a set with the names of all online players, across all connected servers.
     * 
     * @return a set with the names of all online players
     */
    public static Set<String> getOnlinePlayers() {
        return allPlayersOnline.keySet();
    }

    /**
     * Returns a set with the names of all players on the specified server.
     * 
     * @param serverName the name of the server to retrieve online player names from
     * @return a set with the names of all players on the specified server
     */
    public static Set<String> getOnlinePlayers(String serverName) {
        Set<String> onlinePlayers = new HashSet<String>();
        for (String player : allPlayersOnline.keySet()) {
            if (allPlayersOnline.get(player).equals(serverName)) {
                onlinePlayers.add(player);
            }
        }
        return onlinePlayers;
    }

    /**
     * Returns the name of the server the specified player is connected to, or null if they aren't online.
     * 
     * @return the name of the server the specified player is connected to
     */
    public static String getCurrentServer(String playerName) {
        return getOnlinePlayers().contains(playerName) ? allPlayersOnline.get(playerName) : null;
    }

    /**
     * Returns the amount of players on all connected servers.
     * 
     * @return an integer of the amount of players on all servers
     */
    public static int getPlayerCount() {
        return getOnlinePlayers().size();
    }

    /**
     * Returns the amount of players on the specified server.
     * 
     * @param serverName the name of the server to retrieve a player count from
     * @return an integer of the amount of players on the specified server
     */
    public static int getPlayerCount(String serverName) {
        return getOnlinePlayers(serverName).size();
    }

    /**
     * Set the players online for a particular server.
     * 
     * @param serverName the name of the server to set online player names for
     * @param players a set of player names in which to set as online for the specified server
     */
    public static void setOnlinePlayers(String serverName, Set<String> players) {
        allPlayersOnline.values().removeAll(Collections.singleton(serverName));

        for (String player : players) {
            String playerName = player.trim();
            if (playerName.length() > 1 && !playerName.contains(" ")) {
                allPlayersOnline.remove(playerName);
                allPlayersOnline.put(playerName, serverName);
            }
        }
    }

    /**
     * Sends a message through BungeeCord's plugin channel requesting the player list for all connected servers.
     */
    public static void refreshOnlinePlayers() {
        for (AuroraServer server : AuroraServer.values()) {
            refreshOnlinePlayers(server.getName());
        }
    }

    /**
     * Sends a message through BungeeCord's plugin channel requesting the player list for the specified server.
     * 
     * @param serverName the name of the server to request online player names from
     */
    public static void refreshOnlinePlayers(String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerList");
        out.writeUTF(serverName);
        Nova.getInstance().getServer().sendPluginMessage(Nova.getInstance(), "BungeeCord", out.toByteArray());
    }

}
