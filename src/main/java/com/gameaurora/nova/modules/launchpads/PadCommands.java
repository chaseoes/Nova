package com.gameaurora.nova.modules.launchpads;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import com.gameaurora.nova.NovaMessages;

public class PadCommands implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (strings.length == 0) {
            cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + string + " <distance>");
            return true;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
            return true;
        }

        Player player = (Player) cs;
        double distance = 0;

        if (!player.hasPermission("nova.launchpadcommand")) {
            cs.sendMessage(NovaMessages.NO_PERMISSION);
            return true;
        }

        try {
            distance = Double.valueOf(strings[0]);
        } catch (Exception e) {
            cs.sendMessage(NovaMessages.PREFIX_ERROR + "The distance must be a number!");
            return true;
        }

        BlockIterator bi = new BlockIterator(player, 5);
        while (bi.hasNext()) {
            Block b = bi.next();
            if (b.getType() != Material.AIR) {
                PadUtilities.setLaunchDistance(b.getLocation(), distance);
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Set launch pad distance!");
                return true;
            }
        }

        cs.sendMessage(NovaMessages.PREFIX_ERROR + "You must be looking at a block.");
        return true;
    }

}
