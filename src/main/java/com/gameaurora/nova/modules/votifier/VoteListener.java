package com.gameaurora.nova.modules.votifier;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.DataConfiguration;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

// TODO: getPlayer is deprecated. :(
@SuppressWarnings("deprecation")
public class VoteListener implements Listener {

    @EventHandler
    public void onVotifierEvent(VotifierEvent event) {
        Vote vote = event.getVote();
        Nova.getInstance().getServer().broadcastMessage(NovaMessages.BAR);
        Nova.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', NovaMessages.VOTE.replace("%player", vote.getUsername())));
        Nova.getInstance().getServer().broadcastMessage(NovaMessages.VOTE_SITE);
        Nova.getInstance().getServer().broadcastMessage(NovaMessages.BAR);
        Nova.getInstance().getServer().broadcastMessage(NovaMessages.VOTE_CONTEST);
        Nova.getInstance().getServer().broadcastMessage(ChatColor.AQUA + "http://gameaurora.com/contest");
        Nova.getInstance().getServer().broadcastMessage(NovaMessages.BAR);

        for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
        }

        Player player = Nova.getInstance().getServer().getPlayer(vote.getUsername().toLowerCase());
        if (Nova.getInstance().getModule("votifier").getConfig().getBoolean("give-money")) {
            if (player != null) {
                Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), "fe grant " + player.getName() + " 500");
            } else {
                Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), "fe grant " + vote.getUsername() + " 500");
            }
        }

        if (Nova.getInstance().getModule("votifier").getConfig().getBoolean("count-votes")) {
            String user = vote.getUsername().trim().toLowerCase();
            String month = new SimpleDateFormat("MMM").format(Calendar.getInstance().getTime()).toLowerCase();
            String path = "votes." + month + "." + user;
            int votes = DataConfiguration.getConfig().getString(path) != null ? DataConfiguration.getConfig().getInt(path) : 0;
            DataConfiguration.getConfig().set(path, votes + 1);
            DataConfiguration.save();
        }
    }

}
