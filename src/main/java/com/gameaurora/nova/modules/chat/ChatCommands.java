package com.gameaurora.nova.modules.chat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.BungeeUtilities;
import com.gameaurora.nova.utilities.IconMenu;

public class ChatCommands implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("chat")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                return true;
            }

            Player player = (Player) cs;

            if (strings.length == 0) {

            }

            if (strings.length == 1) {
                String playerName = strings[0];
                getMenu(playerName, player).open(player);
            }
        }

        if (cmnd.getName().equalsIgnoreCase("clearchat")) {
            if (cs.hasPermission("nova.clearchat")) {
                for (int i = 0; i <= 120; i++) {
                    Nova.getInstance().getServer().broadcastMessage(" ");
                    BungeeUtilities.broadcastMessage(" ");
                }
            } else {
                cs.sendMessage(NovaMessages.NO_PERMISSION);
            }
        }
        return true;
    }

    private IconMenu getMenu(String playerName, Player openingPlayer) {
        IconMenu menu = new IconMenu(ChatColor.DARK_GRAY + playerName, 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                String optionName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
                String playerName = ChatColor.stripColor(event.getInventory().getTitle());

                if (optionName.toLowerCase().contains("kick")) {
                    event.getPlayer().performCommand("kick " + playerName);
                }

                if (optionName.toLowerCase().contains("ban")) {
                    event.getPlayer().performCommand("ban " + playerName);
                }

                if (optionName.toLowerCase().contains("clear chat")) {
                    event.getPlayer().performCommand("clearchat");
                }

                event.setWillClose(true);
                event.setWillDestroy(true);
            }
        }, Nova.getInstance());

        ItemStack i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 11);
        menu.setOption(3, i, ChatColor.GREEN + "Kick Player", ChatColor.GRAY + "Kick the player for being naughty.");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 14);
        menu.setOption(4, i, ChatColor.GREEN + "Kick Player & Clear Chat", ChatColor.GRAY + "Kick the player and clear the chat.");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 1);
        menu.setOption(5, i, ChatColor.DARK_RED + "Ban Player & Clear Chat", ChatColor.GRAY + "Ban the player and clear the chat.");

        menu.setSpecificTo(openingPlayer);

        return menu;
    }

}
