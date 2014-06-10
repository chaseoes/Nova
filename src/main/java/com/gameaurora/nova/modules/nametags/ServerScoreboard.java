package com.gameaurora.nova.modules.nametags;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.PermissionUtilities;
import com.gameaurora.nova.utilities.bungee.BungeeOnlinePlayerStorage;

public class ServerScoreboard {

    Player player;
    ScoreboardManager manager;
    Scoreboard board;
    List<Team> teams = new ArrayList<Team>();

    Team sidebarTeam;
    Objective sidebarObjective;

    public static String offlinePlayerName = ChatColor.AQUA + "Your Tokens";

    @SuppressWarnings("deprecation")
    public ServerScoreboard(Player player) {
        this.player = player;
        player.setScoreboard(Nova.getInstance().getServer().getScoreboardManager().getNewScoreboard());
        manager = Nova.getInstance().getServer().getScoreboardManager();
        board = manager.getNewScoreboard();

        for (String group : PermissionUtilities.getServiceProvider().getAllGroups()) {
            teams.add(board.registerNewTeam(group));
            getTeam(group).setPrefix(PermissionUtilities.getChatSuffix(group));
        }

        if (Nova.getInstance().moduleIsEnabled("tokens")) {
            sidebarObjective = board.registerNewObjective("TokenObj", "dummy");
            sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            sidebarObjective.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Aurora");
            sidebarTeam = board.registerNewTeam("Tokens");
            sidebarTeam.addPlayer(Nova.getInstance().getServer().getOfflinePlayer(offlinePlayerName));
        }
    }

    @SuppressWarnings("deprecation")
    public void updateBoard() {
        for (String playerName : BungeeOnlinePlayerStorage.getOnlinePlayers()) {
            Team groupTeam = getTeam(PermissionUtilities.getGroupForPlayer(playerName));
            if (!groupTeam.hasPlayer(Nova.getInstance().getServer().getOfflinePlayer(playerName))) {
                groupTeam.addPlayer(Nova.getInstance().getServer().getOfflinePlayer(playerName));
            }
        }

        player.setScoreboard(board);
    }

    private Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }

}
