package com.gameaurora.nova;

import org.bukkit.ChatColor;

public class NovaMessages {

    public static final String PREFIX_GENERAL = ChatColor.AQUA + "[Nova] " + ChatColor.GRAY;
    public static final String PREFIX_ERROR = ChatColor.AQUA + "[Nova] " + ChatColor.RED;
    public static final String NO_PERMISSION = PREFIX_ERROR + "You don't have permission to do that.";
    public static final String MUST_BE_PLAYER = "[Nova] That command can only be used as a player.";
    public static final String CHANGE_SERVER = PREFIX_GENERAL + "Switching servers...";
    public static final String BANNED = ChatColor.RED + "\n\nYou are banned for violating our rules.\nReview our rules at: " + ChatColor.AQUA + "http://gameaurora.com/rules\n\n" + ChatColor.RED + "Proof/reason for ban: " + ChatColor.GRAY;
    public static final String KICKED = PREFIX_ERROR + "You have been kicked for violating our rules.\n\nReview our rules at: " + ChatColor.AQUA + "http://gameaurora.com/rules";
    public static final String ANNOUNCER_PREFIX = ChatColor.GRAY + "[" + ChatColor.GREEN + "" + ChatColor.BOLD + "Aurora" + ChatColor.GRAY + "] ";
    public static final String FIRST_JOIN = "&d%player joined Aurora for the first time!";
    public static final String VOTE = "&a%player &djust voted and received &b2,000 tokens&d!";
    public static final String VOTE_SITE = ChatColor.GREEN + "You can vote for tokens! Go to: " + ChatColor.AQUA + "http://gameaurora.com/vote";
    public static final String BAR = ChatColor.DARK_GRAY + "-----------------------------------------------------";

}
