package com.gameaurora.nova.modules.farmworld;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class FarmworldCommands implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("farmworld")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(NovaMessages.NO_PERMISSION);
                return true;
            }

            World world = Nova.getInstance().getServer().getWorld("farmworld");

            if (strings.length > 0) {
                if (strings[0].equalsIgnoreCase("nether")) {
                    world = Nova.getInstance().getServer().getWorld("farmworld_nether");
                }
            }

            Player player = (Player) cs;
            player.teleport(world.getSpawnLocation());
            player.performCommand("mvspawn");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            cs.sendMessage(NovaMessages.TELEPORT_FARMWORLD);
            return true;
        }
        return true;
    }

}
