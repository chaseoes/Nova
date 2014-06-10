package com.gameaurora.nova.general;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.gameaurora.nova.utilities.bungee.BungeeOnlinePlayerStorage;

public class GeneralTabCompletor implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], new ArrayList<String>(BungeeOnlinePlayerStorage.getOnlinePlayers()), new ArrayList<String>(BungeeOnlinePlayerStorage.getOnlinePlayers().size()));
        } else {
            return new ArrayList<String>(BungeeOnlinePlayerStorage.getOnlinePlayers());
        }
    }
}
