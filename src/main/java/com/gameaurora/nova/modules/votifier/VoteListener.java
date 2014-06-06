package com.gameaurora.nova.modules.votifier;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
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

        for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, 1);
        }

        Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), "fe grant " + vote.getUsername() + " 2000");

        Player player = Nova.getInstance().getServer().getPlayerExact(vote.getUsername());
        if (player != null) {
            player.sendMessage(ChatColor.AQUA + "Thanks for voting, " + ChatColor.GREEN + player.getName() + ChatColor.AQUA + "! Check how much money you have by typing " + ChatColor.GREEN + "/money" + ChatColor.AQUA + "!");
        }
    }

}
