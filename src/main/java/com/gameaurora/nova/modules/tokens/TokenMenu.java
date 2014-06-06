package com.gameaurora.nova.modules.tokens;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;
import com.gameaurora.nova.utilities.IconMenu;

public class TokenMenu {

    public static LinkedHashMap<String, ItemStack> icons = new LinkedHashMap<String, ItemStack>();
    public static HashMap<String, String> descriptions = new HashMap<String, String>();
    public static HashMap<String, IconMenu> players = new HashMap<String, IconMenu>();

    public static void loadIcons() {
        icons.clear();

        ItemStack i = new ItemStack(Material.INK_SACK, 1);
        icons.put("Your Tokens", new ItemStack(Material.SLIME_BALL, 1));

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 1);
        icons.put("Survival", i);
        descriptions.put("Survival", "Plain anti-griefing Minecraft PvE survival!");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 5);
        icons.put("Creative", i);
        descriptions.put("Creative", "Access is limited to those with a creative pass." + "\n" + ChatColor.GRAY + "Purchase one at: " + ChatColor.AQUA + "www.gameaurora.com/store");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 8);
        icons.put("Skyblock", i);
        descriptions.put("Skyblock", "Island survival with limited resources!");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 9);
        icons.put("Spleef", i);
        descriptions.put("Spleef", "Classic spleef, knock out other players!");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 10);
        icons.put("Bow Spleef", i);
        descriptions.put("Bow Spleef", "Spleef with a twist, use a bow!");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 12);
        icons.put("TNT Run", i);
        descriptions.put("TNT Run", "Run around without falling!");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 13);
        icons.put("Deadly Drop", i);
        descriptions.put("Deadly Drop", "Get to the bottom first without dying!");

        i = new ItemStack(Material.INK_SACK, 1);
        i.setDurability((short) 14);
        icons.put("TF2", i);
        descriptions.put("TF2", "Team Fortress 2 in Minecraft!");
    }

    public static void open(Player player) {
        IconMenu menu;
        menu = new IconMenu(ChatColor.DARK_GRAY + "Server Menu", 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                event.getPlayer().sendMessage(NovaMessages.PREFIX_GENERAL + "You clicked!");
                event.setWillClose(true);
                event.setWillDestroy(true);
            }
        }, Nova.getInstance());

        updateOptions(menu);
        menu.setSpecificTo(player);
        menu.open(player);
        players.put(player.getName(), menu);
    }

    public static void destroyCache(Player player) {
        if (players.containsKey(player.getName())) {
            players.get(player.getName()).destroy();
            players.remove(player.getName());
        }
    }

    public static void updateOptions(IconMenu menu) {
        int i = 0;
        for (String server : icons.keySet()) {
            i++;
            String[] descriptionLines = descriptions.get(server).split("\n");
            String[] description = new String[descriptionLines.length + 2];

            int p = 0;
            for (String line : descriptionLines) {
                description[p] = ChatColor.GRAY + line;
                p++;
            }

            description[p] = " ";
            int playerCount = PlayerCountUtilities.getPlayerCount(server);
            String playerCountMessage = ChatColor.AQUA + "" + playerCount;
            if (playerCount == -1) {
                playerCountMessage = ChatColor.RED + "Server Offline";
            }

            description[p + 1] = ChatColor.YELLOW + "Players Online: " + playerCountMessage;
            menu.setOption(i - 1, icons.get(server), ChatColor.GREEN + server, description);
        }
        menu.reload();
    }

}