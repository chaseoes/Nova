package com.gameaurora.nova.modules.permissions;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import com.gameaurora.nova.Nova;

public class PermissionUtilities {

	static PermissionUtilities instance = new PermissionUtilities();
	HashMap<String, PermissionAttachment> attachments = new HashMap<String, PermissionAttachment>();
	HashMap<String, String> prefixes = new HashMap<String, String>();
	HashMap<String, String> suffixes = new HashMap<String, String>();
	HashMap<String, String> groups = new HashMap<String, String>();
	
    public static PermissionUtilities getUtilities() {
        return instance;
    }

	public PermissionAttachment getAttachment(Player player) {
		return attachments.get(player.getName().toLowerCase());
	}

	public void addAttachment(Player player) {
		if (attachments.containsKey(player.getName().toLowerCase())) {
			attachments.get(player.getName().toLowerCase()).remove();
		}
		PermissionAttachment attachment = player.addAttachment(Nova.getInstance());
		attachments.put(player.getName().toLowerCase(), attachment);
	}

	public HashMap<String, Boolean> getPermissions(Player player) {
		HashMap<String, Boolean> perms = new HashMap<String, Boolean>();
		ArrayList<String> groups = new ArrayList<String>();
		for (String group : PermissionsDataFile.getDataFile().getStringList("groups." + getGroup(player) + ".inherits")) {
			groups.add(group);
		}
		groups.add(getGroup(player));
		for (String group : groups) {
			for (String permission : PermissionsDataFile.getDataFile().getStringList("groups." + group + ".permissions")) {
				String perm = permission.replace("^", "");
				if (permission.contains("^")) {
					perms.put(perm, false);
				} else {
					perms.put(perm, true);
				}
			}
		}
		return perms;
	}

	public void refreshPermissions(Player player) {
		addAttachment(player);
		for (String permission : getPermissions(player).keySet()) {
			getAttachment(player).setPermission(permission, getPermissions(player).get(permission));
		}
	}

	public void refreshPermissions() {
		for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
			setupPlayer(player);
		}
	}

	public String getGroup(Player player) {
		return getGroup(player.getName());
	}

	public String getGroup(String name) {
		String group = PermissionsDataFile.getDataFile().getString("users." + name.toLowerCase());
		if (group != null) {
			return group;
		}
		return Nova.getInstance().getConfig().getString("permissions.default-group");
	}

	public String getPrefix(Player player) {
		return getPrefix(player.getName());
	}

	public String getPrefix(String name) {
		return PermissionsDataFile.getDataFile().getString("groups." + getGroup(name) + ".prefix");
	}

	public String getSuffix(Player player) {
		return getSuffix(player.getName());
	}

	public String getSuffix(String name) {
		return PermissionsDataFile.getDataFile().getString("groups." + getGroup(name) + ".suffix");
	}

	public void setGroup(Player player, String group) {
		PermissionsDataFile.getDataFile().set("users." + player.getName().toLowerCase(), group);
		PermissionsDataFile.save();
		refreshPermissions(player);
	}

	public void setGroup(String player, String group) {
		PermissionsDataFile.getDataFile().set("users." + player.toLowerCase(), group);
		PermissionsDataFile.save();
	}

	public void setupPlayer(Player player) {
		String group = getGroup(player);
		groups.put(player.getName(), group);
		refreshPermissions(player);
		prefixes.put(group, PermissionsDataFile.getDataFile().getString("groups." + group + ".prefix"));
		suffixes.put(group, PermissionsDataFile.getDataFile().getString("groups." + group + ".suffix"));
	}

	public String colorize(String s) {
		if (s == null) {
			return null;
		}
		return s.replaceAll("&([l-ok0-8k9a-f])", "\u00A7$1");
	}
	
	public void onEnable() {
		PermissionsDataFile.getDataFile().options().copyDefaults(true);
        PermissionsDataFile.save();
        PermissionUtilities.getUtilities().refreshPermissions();
        Nova.getInstance().getServer().getPluginManager().registerEvents(new PermissionListeners(), Nova.getInstance());
        
        for (Player p : Nova.getInstance().getServer().getOnlinePlayers()) {
            setupPlayer(p);
        }
	}
	
	public void onDisable() {
        PermissionsDataFile.save();
        for (Player p : Nova.getInstance().getServer().getOnlinePlayers()) {
            if (attachments.containsKey(p.getName())) {
                attachments.get(p.getName()).remove();
            }
        }
	}

}
