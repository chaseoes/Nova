package com.gameaurora.nova.utilities;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionUtilities {
	
	private static final String GROUP_PREFIX = "group.";
	
	public static String getGroupForPlayer(Player player) {
		return getGroupsForPlayer(player).iterator().next();
	}
	
	public static Set<String> getGroupsForPlayer(Player player) {
	    Set<String> groups = new HashSet<String>();
	    for (PermissionAttachmentInfo pai : player.getEffectivePermissions()) {
	        if (!pai.getPermission().startsWith(GROUP_PREFIX) || !pai.getValue())
	            continue;
	        groups.add(pai.getPermission().substring(GROUP_PREFIX.length()));
	    }
	    return groups;
	}

}
