package com.gameaurora.nova.modules.nametags;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.PermissionUtilities;

public class ServerScoreboard {

	private static ScoreboardManager manager;
	private static Scoreboard board;
	private static List<Team> teams = new ArrayList<Team>();

	public static void load() {
		manager = Nova.getInstance().getServer().getScoreboardManager();
		board = manager.getNewScoreboard();

		for (String group : PermissionUtilities.getServiceProvider().getAllGroups()) {
			addTeam(group);
		}
	}

	public static void addTeam(String name) {
		teams.add(board.registerNewTeam(name));
		getTeam(name).setPrefix(PermissionUtilities.getChatSuffix(name));

	}

	public static Team getTeam(String name) {
		for (Team team : teams) {
			if (team.getName().equals(name)) {
				return team;
			}
		}
		return null;
	}

	public static void updateBoard() {
		for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
			if (!(player.getScoreboard() == board)) {
				player.setScoreboard(board);
			}

			Team groupTeam = getTeam(PermissionUtilities.getGroupForPlayer(player));
			if (!groupTeam.hasPlayer(player)) {
				groupTeam.addPlayer(player);
			}
		}
	}

	public static void clear() {
		for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
			player.setScoreboard(Nova.getInstance().getServer().getScoreboardManager().getNewScoreboard());
		}

		teams.clear();
		manager = Nova.getInstance().getServer().getScoreboardManager();
		board = Nova.getInstance().getServer().getScoreboardManager().getNewScoreboard();
	}

}
