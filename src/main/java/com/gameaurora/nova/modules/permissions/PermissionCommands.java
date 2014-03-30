package com.gameaurora.nova.modules.permissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class PermissionCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (strings.length == 0) {
			cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + cmnd.getLabel() + " <reload|setgroup|clean>");
			return true;
		}

		if (strings[0].equalsIgnoreCase("reload")) {
			if (cs.hasPermission("nova.permissions.reload")) {
				Nova.getInstance().reloadConfig();
				Nova.getInstance().saveConfig();
				PermissionsDataFile.reload();
				PermissionsDataFile.save();
				PermissionUtilities.getUtilities().refreshPermissions();
				cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Reloaded the configuration!");
				return true;
			} else {
				cs.sendMessage(NovaMessages.NO_PERMISSION);
			}
		}

		if (strings[0].equalsIgnoreCase("setgroup")) {
			if (cs.hasPermission("nova.permissions.setgroup")) {
				if (strings.length == 3) {
					Player p = Nova.getInstance().getServer().getPlayer(strings[1]);
					String group = strings[2];
					if (p != null) {
						PermissionUtilities.getUtilities().setGroup(p, group);
						PermissionsDataFile.reload();
						PermissionsDataFile.save();
						PermissionUtilities.getUtilities().refreshPermissions(p);
						cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully set " + p.getName() + "'s group to " + group + "!");
					} else {
						PermissionUtilities.getUtilities().setGroup(strings[1], group);
						PermissionsDataFile.reload();
						PermissionsDataFile.save();
						cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully set " + strings[1] + "'s group to " + group + "!");
					}
				} else {
					cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + cmnd.getLabel() + " setgroup <player>");
				}
			} else {
				cs.sendMessage(NovaMessages.NO_PERMISSION);
			}
		}

		if (strings[0].equalsIgnoreCase("clean")) {
			if (cs.hasPermission("nova.permissions.clean")) {
				cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Starting cleanup...");
				int i = 0;
				for (String player : PermissionsDataFile.getDataFile().getConfigurationSection("users").getKeys(false)) {
					if (PermissionsDataFile.getDataFile().getString("users." + player).equals("default")) {
						PermissionsDataFile.getDataFile().set("users." + player, null);
						i++;
					}
				}
				PermissionsDataFile.save();
				cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Cleanup successful (removed " + i + " players).");
			} else {
				cs.sendMessage(NovaMessages.NO_PERMISSION);
			}
		}

		return true;
	}

}
