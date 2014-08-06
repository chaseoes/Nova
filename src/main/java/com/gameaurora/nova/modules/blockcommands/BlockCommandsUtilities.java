package com.gameaurora.nova.modules.blockcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.DataConfiguration;
import com.gameaurora.nova.utilities.SerializableLocation;

public class BlockCommandsUtilities {

    public static void setBlockCommand(Location location, String command) {
        DataConfiguration.getConfig().set("block-commands." + SerializableLocation.locationToString(location), command);
        DataConfiguration.save();
    }

    public static String getBlockCommand(Location location) {
        return DataConfiguration.getConfig().getString("block-commands." + SerializableLocation.locationToString(location));
    }

    public static void runBlockCommand(Location location, Player player) {
        String fullCommand = getBlockCommand(location);
        if (fullCommand != null) {
            String[] splitCommand = fullCommand.split(":");
            if (splitCommand.length > 0) {
                String commandType = splitCommand[0];
                String command = fullCommand.replace(splitCommand[0] + ":", "").replace("%player", player.getName());
                if (commandType.equals("p")) {
                    player.performCommand(command);
                }

                if (commandType.equals("c")) {
                    Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), command);
                }

                if (commandType.equals("l")) {
                    Location teleportLocation = SerializableLocation.stringToLocation(command);
                    if (teleportLocation != null) {
                        player.teleport(teleportLocation);
                    }
                }
            }
        }
    }

}
