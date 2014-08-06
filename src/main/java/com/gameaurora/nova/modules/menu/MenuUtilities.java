package com.gameaurora.nova.modules.menu;

import org.bukkit.ChatColor;

import com.gameaurora.nova.general.AuroraServer;

public class MenuUtilities {

    public static String[] getDescription(AuroraServer server) {
        String[] descriptionLines = { "" };
        descriptionLines = server.getDescription().split("\n");

        String[] description = new String[descriptionLines.length + 2];

        int p = 0;
        for (String line : descriptionLines) {
            description[p] = ChatColor.GRAY + line;
            p++;
        }

        description[p] = " ";
        int playerCount = server.getPlayerCount();
        System.out.println("COUNT: " + server.getPlayerCount());
        String playerCountMessage = ChatColor.AQUA + "" + playerCount;
        if (playerCount == -1) {
            playerCountMessage = ChatColor.RED + "Server Offline";
            System.out.print("OFFLINE");
        }

        description[p + 1] = ChatColor.YELLOW + "Players Online: " + playerCountMessage;
        return description;
    }

}
