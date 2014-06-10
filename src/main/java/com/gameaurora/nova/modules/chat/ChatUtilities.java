package com.gameaurora.nova.modules.chat;

import java.util.ArrayList;
import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;

import com.gameaurora.nova.utilities.GeneralUtilities;

public class ChatUtilities {

    public static List<String> bannedWords = new ArrayList<String>();

    public static boolean tooManyCaps(String message) {
        double capsCountt = 0.0D;
        double msglength = message.length();
        for (char c : message.toCharArray()) {
            if (Character.isUpperCase(c)) {
                capsCountt += 1.0D;
            }

            if (!Character.isLetterOrDigit(c)) {
                msglength -= 1.0D;
            }
        }

        int percent = 75;
        double calc = capsCountt / msglength * 100.0D;
        if (calc >= percent) {
            if (message.length() > 3) {
                return true;
            }
        }
        return false;
    }

    public static final FancyMessage buildFancyChatMessage(String format, String playerName, String message, String prettyServerName) {
        String[] finalMessage = String.format(format, playerName, message).split(":");
        FancyMessage fm = new FancyMessage(ChatColor.translateAlternateColorCodes('&', finalMessage[0]) + ChatColor.WHITE + ": ");
        ChatColor color = (format.split(":")[1].contains("c")) ? ChatColor.RED : ChatColor.WHITE;
        String server = (prettyServerName.equalsIgnoreCase(GeneralUtilities.getPrettyServerName())) ? playerName + " is on the same server as you!" : GeneralUtilities.punctuateName(playerName) + " Current Server: " + ChatColor.AQUA + prettyServerName;
        fm = fm.tooltip(ChatColor.GREEN + server).then(message).color(color);
        return fm;
    }

    public static void loadBannedWords() {
        bannedWords.add("fuck");
        bannedWords.add("bitch");
        bannedWords.add("cunt");
        bannedWords.add("whore");
        bannedWords.add("faggot");
        bannedWords.add("nigga");
        bannedWords.add("nigger");
        bannedWords.add("queer");
        bannedWords.add("penis");
        bannedWords.add("dick");
        bannedWords.add("blowjob");
        bannedWords.add("blow job");
        bannedWords.add("whalecum");
        bannedWords.add("whale cum");
        bannedWords.add("dildo");
        bannedWords.add("douche");
        bannedWords.add("dumbass");
        bannedWords.add("retarded");
        bannedWords.add("incest");
        bannedWords.add("shit");
        bannedWords.add("ass");
    }

}
