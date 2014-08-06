package com.gameaurora.nova.modules.infocommands;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class InfoCommands implements CommandExecutor {

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (cmnd.getName().equalsIgnoreCase("help")) {
            String serverName = GeneralUtilities.getServerName();
            cs.sendMessage(ChatColor.DARK_GRAY + "<----------------" + ChatColor.GREEN + ChatColor.BOLD + " Aurora Commands " + ChatColor.DARK_GRAY + "---------------->");
            cs.sendMessage(ChatColor.AQUA + "/msg <player> <message>" + ChatColor.GRAY + ": Send a private message.");
            cs.sendMessage(ChatColor.AQUA + "/member" + ChatColor.GRAY + ": Information on how to get the member rank.");
            cs.sendMessage(ChatColor.AQUA + "/vote" + ChatColor.GRAY + ": View information on voting.");
            cs.sendMessage(ChatColor.AQUA + "/rules" + ChatColor.GRAY + ": View the network rules.");
            cs.sendMessage(ChatColor.AQUA + "/mumble" + ChatColor.GRAY + ": Information on our Mumble server.");
            if (serverName.equals("survival") || serverName.equals("creative") || serverName.equals("buildteam")) {
                cs.sendMessage(ChatColor.AQUA + "/home" + ChatColor.GRAY + ": Teleport to your home.");
                cs.sendMessage(ChatColor.AQUA + "/home set" + ChatColor.GRAY + ": Set your home location.");

                if (serverName.equals("survival")) {
                    cs.sendMessage(ChatColor.AQUA + "/cmodify <player>" + ChatColor.GRAY + ": Let a friend access a chest.");
                    cs.sendMessage(ChatColor.AQUA + "/livemap" + ChatColor.GRAY + ": View the dynamic server map.");
                }
            }

            if (serverName.equals("skyblock")) {
                cs.sendMessage(ChatColor.AQUA + "/island" + ChatColor.GRAY + ": Open the Skyblock menu.");
                cs.sendMessage(ChatColor.AQUA + "/island invite <player>" + ChatColor.GRAY + ": Add a player to your island.");
                cs.sendMessage(ChatColor.AQUA + "/island top" + ChatColor.GRAY + ": View the top islands.");
            }

            if (serverName.equals("tf2")) {
                cs.sendMessage(ChatColor.AQUA + "/tf2 leave" + ChatColor.GRAY + ": Leave your current TF2 game.");
            }

            if (serverName.equals("spleef") || serverName.equals("bowspleef") || serverName.equals("tntrun") || serverName.equals("deadlydrop")) {
                cs.sendMessage(ChatColor.AQUA + "/ag vote" + ChatColor.GRAY + ": Vote to start a game.");
                cs.sendMessage(ChatColor.AQUA + "/ag leave" + ChatColor.GRAY + ": Leave the current arena.");
            }
        }

        if (cmnd.getName().equalsIgnoreCase("vote")) {
            if (GeneralUtilities.getServerName().equalsIgnoreCase("hub")) {
                cs.sendMessage(NovaMessages.BAR);
            }

            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Click on the following URL to vote for us:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/vote");

            if (GeneralUtilities.getServerName().equalsIgnoreCase("hub")) {
                cs.sendMessage(NovaMessages.BAR);
                String user = cs.getName().trim().toLowerCase();
                String month = new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime()).toLowerCase();
                String path = "votes." + month + "." + user;
                int votes = Nova.getInstance().getConfig().getString(path) != null ? Nova.getInstance().getConfig().getInt(path) : 0;
                cs.sendMessage(ChatColor.GREEN + "You have voted " + ChatColor.AQUA + votes + ChatColor.GREEN + " times this month.");
                cs.sendMessage(ChatColor.GRAY + "Remember to spell your username properly (it is case-sensitive) to have your votes count.");
                cs.sendMessage(NovaMessages.BAR);
                // cs.sendMessage(ChatColor.GREEN + getTopVoters(5));
                // cs.sendMessage(NovaMessages.BAR);
            }
        }

        if (cmnd.getName().equalsIgnoreCase("member")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Follow the instructions here to become a member:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/member");
        }

        if (cmnd.getName().equalsIgnoreCase("rules")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Click on the following URL to view our rules:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/rules");
        }

        if (cmnd.getName().equalsIgnoreCase("tos")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Click on the following URL to view our terms:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/tos");
        }

        if (cmnd.getName().equalsIgnoreCase("privacy")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Click on the following URL to view our privacy policy:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/privacy");
        }

        if (cmnd.getName().equalsIgnoreCase("store")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Click on the following URL to view our store:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/store");
        }

        if (cmnd.getName().equalsIgnoreCase("livemap")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Click on the following URL to view the livemap:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.AQUA + "http://gameaurora.com/livemap");
        }

        if (cmnd.getName().equalsIgnoreCase("mumble")) {
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Our Mumble information is as follows:");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + ChatColor.GREEN + "gameaurora.com (Port 64738)");
            cs.sendMessage(NovaMessages.PREFIX_GENERAL + "You can also click here: " + ChatColor.AQUA + "http://gameaurora.com/mumble");
        }
        return true;
    }

    public static String getTopVoters(int size) {
        String topVoter = "";
        int topVote = 0;
        for (String player : Nova.getInstance().getConfig().getConfigurationSection("votes.jul").getKeys(false)) {
            int v = Nova.getInstance().getConfig().getInt("votes." + "jul" + "." + player);
            if (v > topVote) {
                if (!player.equalsIgnoreCase("missrandall") && !player.equalsIgnoreCase("gnisl") && !player.equalsIgnoreCase("immortalmuggle") && !player.equalsIgnoreCase("budderking0909")) {
                    topVote = v;
                    topVoter = player;
                }
            }
        }

        return topVoter;
    }

}
