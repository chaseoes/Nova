package com.gameaurora.nova.utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import com.gameaurora.nova.Nova;

public class PermissionUtilities {

	private static ZPermissionsService service = Nova.getInstance().getServer().getServicesManager().load(ZPermissionsService.class);

	public static String getGroupForPlayer(Player player) {
		return service.getPlayerPrimaryGroup(player.getUniqueId());
	}

	public static String getChatFormat(Player player) {
		try {
			String prefix = service.getGroupMetadata(getGroupForPlayer(player), "prefix", Object.class).toString();
			return ChatColor.translateAlternateColorCodes('&', prefix.replace("<player>", "%1$s").replace("<message>", "%2$s"));
		} catch (Exception e) {
			return ChatColor.RED + getGroupForPlayer(player) + " CHAT ERROR" + "\n" + player.getUniqueId().toString();
		}

	}
}
