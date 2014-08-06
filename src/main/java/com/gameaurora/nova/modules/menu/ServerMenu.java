package com.gameaurora.nova.modules.menu;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.general.AuroraServer;
import com.gameaurora.nova.utilities.BungeeUtilities;
import com.gameaurora.nova.utilities.IconMenu;

public class ServerMenu {

    public static void open(Player player) {
        int size = (player.hasPermission("nova.buildteam") || player.hasPermission("nova.extraservers")) ? 36 : 18;
        IconMenu menu = new IconMenu(ChatColor.DARK_GRAY + "Server Menu", size, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                BungeeUtilities.sendToServer(event.getPlayer(), ChatColor.stripColor(event.getName()).toLowerCase().replace(" ", ""));
                event.setWillClose(true);
                event.setWillDestroy(true);
            }
        }, Nova.getInstance());

        refresh(menu, player);
        menu.setSpecificTo(player);
        menu.open(player);
    }

    private static void refresh(IconMenu menu, Player player) {
        addServer(menu, AuroraServer.HUB, 2);
        addServer(menu, AuroraServer.SURVIVAL, 3);
        addServer(menu, AuroraServer.CREATIVE, 4);
        addServer(menu, AuroraServer.FACTIONS, 5);
        addServer(menu, AuroraServer.SKYBLOCK, 6);

        addServer(menu, AuroraServer.SPLEEF, 11);
        addServer(menu, AuroraServer.BOWSPLEEF, 12);
        addServer(menu, AuroraServer.TNTRUN, 13);
        addServer(menu, AuroraServer.DEADLYDROP, 14);
        addServer(menu, AuroraServer.TF2, 15);

        if (menu.getSize() > 18) {
            if (player.hasPermission("nova.buildteam")) {
                addServer(menu, AuroraServer.BUILDTEAM, 30);
            }

            if (player.hasPermission("nova.extraservers")) {
                addServer(menu, AuroraServer.LIMBO, 31);
                addServer(menu, AuroraServer.SANDBOX, 32);
            }
        }

        menu.reload();
    }

    private static void addServer(IconMenu menu, AuroraServer server, int position) {
        menu.setOption(position, server.getIcon(), ChatColor.GREEN + server.getPrettyName(), MenuUtilities.getDescription(server));
    }

}
