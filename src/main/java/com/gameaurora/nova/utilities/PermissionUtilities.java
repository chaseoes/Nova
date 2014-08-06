package com.gameaurora.nova.utilities;

import java.util.Set;

import org.bukkit.entity.Player;

public class PermissionUtilities {

    public static Set<String> getGroups() {
        return com.gameaurora.milkyway.utilities.PermissionUtilities.getGroups();
    }

    public static String getPrimaryGroup(Player player) {
        return com.gameaurora.milkyway.utilities.PermissionUtilities.getPrimaryGroup(player);
    }

    public static String getPrefix(String group) {
        return com.gameaurora.milkyway.utilities.PermissionUtilities.getPrefix(group);
    }

    public static String getSuffix(String group) {
        return com.gameaurora.milkyway.utilities.PermissionUtilities.getSuffix(group);
    }

}
