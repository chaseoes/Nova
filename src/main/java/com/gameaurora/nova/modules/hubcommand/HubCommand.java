package com.gameaurora.nova.modules.hubcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class HubCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (!(cs instanceof Player)) {
			cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
			return true;
		}
		
		Player player = (Player) cs;
		GeneralUtilities.sendToServer(player, "hub");
		return true;
	}

}