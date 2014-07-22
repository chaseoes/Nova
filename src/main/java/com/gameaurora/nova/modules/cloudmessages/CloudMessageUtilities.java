package com.gameaurora.nova.modules.cloudmessages;

public class CloudMessageUtilities {

    public static final String delimiter = "|";

    public static String createChatMessageString(String format, String playerName, String message) {
        return format + delimiter + playerName + delimiter + message; // format|playerName|message
    }

    public static String createPrivateMessageString(String message, String fromName, String toName) {
        return message + delimiter + fromName + delimiter + toName; // message|fromName|toName
    }

    public static String createPlayerLoginString(String playerName, String playerRank) {
        return playerName + delimiter + playerRank; // playerName|playerRank
    }

}
