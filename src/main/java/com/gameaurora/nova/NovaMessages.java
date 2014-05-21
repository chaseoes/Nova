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
    
}
