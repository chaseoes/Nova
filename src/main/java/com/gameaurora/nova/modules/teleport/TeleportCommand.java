package com.gameaurora.nova.modules.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;

public class TeleportCommand implements CommandExecutor {

    // TODO: getPlayer is deprecated. :(
    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("teleport")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                return true;
            }

            if (strings.length != 1) {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + cmnd.getLabel() + " <player name>");
                return true;
            }

            if (!cs.hasPermission("nova.tp")) {
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + "You must be " + ChatColor.GREEN + "VIP" + ChatColor.GRAY + " or above to do that!");
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Buy a rank here: " + ChatColor.AQUA + "http://gameaurora.com/store");
                return true;
            }

            Player player = (Player) cs;
            Player teleportTo = Nova.getInstance().getServer().getPlayer(strings[0]);

            if (Nova.getInstance().getServer().getPlayer(strings[0]) != null) {
                if (!cs.hasPermission("nova.tpbypass")) {
                    if (teleportTo.hasMetadata("nova.tp.disabled")) {
                        cs.sendMessage(NovaMessages.PREFIX_ERROR + teleportTo.getName() + " has teleport requests turned off.");
                        return true;
                    }

                    clearUnacceptedRequests(teleportTo);
                    TeleportRequest request = new TeleportRequest(player, teleportTo);
                    request.send();
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "A teleport request has been sent to " + ChatColor.GREEN + teleportTo.getDisplayName() + ChatColor.GRAY + ".");
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "They must type " + ChatColor.GREEN + "/tpa" + ChatColor.GRAY + " to accept.");
                    Nova.getInstance().teleportData.teleportRequests.add(request);
                    return true;
                } else {
                    player.teleport(teleportTo.getLocation());
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "Teleported to " + ChatColor.GREEN + teleportTo.getName() + ChatColor.GRAY + ".");
                    return true;
                }
            } else {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + strings[0] + " isn't online.");
            }
            return true;
        }

        if (cmnd.getName().equalsIgnoreCase("teleporthere")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                return true;
            }

            if (strings.length != 1) {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + cmnd.getLabel() + " <player name>");
                return true;
            }

            if (!cs.hasPermission("nova.tphere")) {
                cs.sendMessage(NovaMessages.NO_PERMISSION);
                return true;
            }

            Player player = (Player) cs;
            Player teleportHere = Nova.getInstance().getServer().getPlayer(strings[0]);

            if (Nova.getInstance().getServer().getPlayer(strings[0]) != null) {
                if (!cs.hasPermission("nova.tpbypass")) {
                    if (teleportHere.hasMetadata("nova.tp.disabled")) {
                        cs.sendMessage(NovaMessages.PREFIX_ERROR + strings[0] + " has teleport requests turned off.");
                        return true;
                    }

                    clearUnacceptedRequests(teleportHere);
                    TeleportHereRequest request = new TeleportHereRequest(player, teleportHere);
                    request.send();
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "A teleport request has been sent to " + ChatColor.GREEN + teleportHere.getDisplayName() + ChatColor.GRAY + ".");
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "They must type " + ChatColor.GREEN + "/tpa" + ChatColor.GRAY + " to accept.");
                    Nova.getInstance().teleportData.teleportHereRequests.add(request);
                    return true;
                } else {
                    teleportHere.teleport(player.getLocation());
                    teleportHere.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + player.getName() + ChatColor.GRAY + " teleported you to them.");
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + teleportHere.getName() + ChatColor.GRAY + " was teleported to you.");
                    return true;
                }
            } else {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + strings[0] + " isn't online.");
            }

            return true;
        }

        if (cmnd.getName().equalsIgnoreCase("teleportaccept")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                return true;
            }

            if (strings.length != 0) {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + cmnd.getLabel());
                return true;
            }

            Player player = (Player) cs;

            for (TeleportRequest request : Nova.getInstance().teleportData.teleportRequests) {
                if (request.getPlayerToTeleportTo().getName().equals(player.getName())) {
                    request.getRequestingPlayer().teleport(request.getPlayerToTeleportTo().getLocation());
                    request.getRequestingPlayer().sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + request.getPlayerToTeleportTo().getDisplayName() + ChatColor.GRAY + " accepted your teleport request.");
                    Nova.getInstance().teleportData.teleportRequests.remove(request);
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully accepted request from " + ChatColor.GREEN + request.getRequestingPlayer().getDisplayName() + ChatColor.GRAY + ".");
                    return true;
                }
            }

            for (TeleportHereRequest request : Nova.getInstance().teleportData.teleportHereRequests) {
                if (request.getPlayerToPlayerRequesting().getName().equals(player.getName())) {
                    request.getPlayerToPlayerRequesting().teleport(request.getRequestingPlayer().getLocation());
                    request.getRequestingPlayer().sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + request.getPlayerToPlayerRequesting().getDisplayName() + ChatColor.GRAY + " accepted your teleport request.");
                    Nova.getInstance().teleportData.teleportHereRequests.remove(request);
                    player.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully accepted request from " + ChatColor.GREEN + request.getRequestingPlayer().getDisplayName() + ChatColor.GRAY + ".");
                    return true;
                }
            }

            cs.sendMessage(NovaMessages.PREFIX_ERROR + "You have no pending teleport requests to accept.");
            return true;
        }

        if (cmnd.getName().equalsIgnoreCase("teleportdeny")) {
            return true;
        }

        if (cmnd.getName().equalsIgnoreCase("teleporttoggle")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                return true;
            }

            if (strings.length != 0) {
                cs.sendMessage(NovaMessages.PREFIX_ERROR + "Usage: /" + cmnd.getLabel());
                return true;
            }

            if (!cs.hasPermission("nova.tptoggle")) {
                cs.sendMessage(NovaMessages.NO_PERMISSION);
                return true;
            }

            Player player = (Player) cs;
            toggleTeleports(player);
            return true;
        }
        return true;
    }

    private void clearUnacceptedRequests(Player player) {
        TeleportRequest teleportRequest = null;
        TeleportHereRequest teleportHereRequest = null;

        for (TeleportRequest request : Nova.getInstance().teleportData.teleportRequests) {
            if (request.getPlayerToTeleportTo().getName().equals(player.getName())) {
                teleportRequest = request;
            }
        }

        for (TeleportHereRequest request : Nova.getInstance().teleportData.teleportHereRequests) {
            if (request.getPlayerToPlayerRequesting().getName().equals(player.getName())) {
                teleportHereRequest = request;
            }
        }

        Nova.getInstance().teleportData.teleportRequests.remove(teleportRequest);
        Nova.getInstance().teleportData.teleportHereRequests.remove(teleportHereRequest);
    }

    private void toggleTeleports(Player player) {
        if (player.hasMetadata("nova.tp.disabled")) {
            player.removeMetadata("nova.tp.disabled", Nova.getInstance());
            player.sendMessage(NovaMessages.PREFIX_GENERAL + "Teleporting has been toggled on.");
        } else {
            player.setMetadata("nova.tp.disabled", new FixedMetadataValue(Nova.getInstance(), true));
            player.sendMessage(NovaMessages.PREFIX_GENERAL + "Teleporting has been toggled off.");
        }
    }

}
