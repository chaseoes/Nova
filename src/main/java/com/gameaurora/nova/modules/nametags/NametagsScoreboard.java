package com.gameaurora.nova.modules.nametags;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.PermissionUtilities;

public class NametagsScoreboard {

    private static NametagsScoreboard instance = new NametagsScoreboard();

    private List<Team> teams = new ArrayList<Team>();
    private ScoreboardManager scoreboardManager;
    private Scoreboard board;

    public NametagsScoreboard() {
        scoreboardManager = Nova.getInstance().getServer().getScoreboardManager();
        board = scoreboardManager.getNewScoreboard();

        for (String group : PermissionUtilities.getGroups()) {
            teams.add(board.registerNewTeam(group));
            getTeam(group).setPrefix(ChatColor.getByChar(PermissionUtilities.getSuffix(group).charAt(0)) + "");
        }
    }

    public static NametagsScoreboard getInstance() {
        return instance;
    }

    public void update() {
        for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
            update(player);
        }
    }

    public void update(Player player) {
        Team groupTeam = getTeam(PermissionUtilities.getPrimaryGroup(player));
        if (!groupTeam.hasPlayer(player)) {
            groupTeam.addPlayer(player);
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
