package com.gameaurora.nova.modules.cloudmessages;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;

import com.gameaurora.nova.utilities.GeneralUtilities;

public class CloudMessageUtilities {

    public static final String delimiter = "|";

    public static String createChatMessageString(String format, String playerName, String message) {
        return format + delimiter + playerName + delimiter + message; // format|playerName|message
    }

    public static FancyMessage splitUnformattedChatMessageString(String message, String prettyServerName) {
        String[] splitMessage = message.split("\\" + delimiter);
        String finalMessage = String.format(splitMessage[0], splitMessage[1], splitMessage[2]);
        return new FancyMessage(finalMessage).tooltip(ChatColor.GREEN + GeneralUtilities.punctuateName(splitMessage[1]) + " Current Server: " + ChatColor.AQUA + prettyServerName);
    }

    public static String createPrivateMessageString(String message, String fromPlayerName, String toPlayerName) {
        return message + delimiter + fromPlayerName + delimiter + toPlayerName; // format|playerName|message
    }

    public static FancyMessage sendUnformattedPrivateMessageString(String message, String prettyServerName) {
        String[] splitMessage = message.split("\\" + delimiter);
        // String privateMessage = splitMessage[0];
        // String fromPlayer = splitMessage[1];
        // String toPlayer = splitMessage[2];
        return new FancyMessage("").tooltip(ChatColor.GREEN + GeneralUtilities.punctuateName(splitMessage[1]) + " Current Server: " + ChatColor.AQUA + prettyServerName); // TODO: http://i.imgur.com/gNOohYb.png
    }

}
