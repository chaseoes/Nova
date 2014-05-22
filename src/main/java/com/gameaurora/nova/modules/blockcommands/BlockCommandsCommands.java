package com.gameaurora.nova.modules.blockcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.gameaurora.nova.NovaMessages;

public class BlockCommandsCommands implements CommandExecutor {

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		String usageError = NovaMessages.PREFIX_ERROR + "Usage: /" + string + " <p|c|l>:[command]";

		if (strings.length == 0) {
			cs.sendMessage(usageError);
			return true;
		}

		if (!cs.hasPermission("nova.blockcommands")) {
			cs.sendMessage(NovaMessages.NO_PERMISSION);
			return true;
		}

		if (!(cs instanceof Player)) {
			cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
			return true;
		}

		Player player = (Player) cs;

		if (!strings[0].contains(":")) {
			cs.sendMessage(usageError);
			return true;
		}

		BlockIterator bi = new BlockIterator(player, 5);
		while (bi.hasNext()) {
			Block b = bi.next();
			if (b.getType() != Material.AIR) {
				BlockCommandsUtilities.setBlockCommand(b.getLocation(), StringUtils.join(strings, " ").replace(StringUtils.join(strings).split(":") + ":", ""));
				cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Created block command successfully.");
				return true;
			}
		}

		return true;
	}

}
