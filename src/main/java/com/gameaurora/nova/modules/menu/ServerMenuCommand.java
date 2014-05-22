package com.gameaurora.nova.modules.menu;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.NovaMessages;

public class ServerMenuCommand implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
			return true;
		}

		Player player = (Player) cs;

		for (String server : MenuUtilities.icons.keySet()) {
			PlayerCountUtilities.requestPlayerCount(server);
		}

		MenuUtilities.destroyCache(player);
		MenuUtilities.open(player);
		return true;
	}

}
