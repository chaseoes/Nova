package com.gameaurora.nova.modules.announcer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class AnnouncerTask extends BukkitRunnable {

    private static int lastAnnounced = -1;

    public void run() {
        String announcement = getAnnouncement(lastAnnounced + 1);
        if (announcement != null) {
            announce(announcement);
            lastAnnounced = lastAnnounced + 1;
        } else {
            announce(getAnnouncement(0));
            lastAnnounced = 0;
        }
    }

    private void announce(String message) {
        for (Player player : Nova.getInstance().getServer().getOnlinePlayers()) {
            player.sendMessage(NovaMessages.ANNOUNCER_PREFIX + ChatColor.translateAlternateColorCodes('&', message.replace("%player", player.getName()).replace("%server", GeneralUtilities.getPrettyServerName())));
        }
    }

    private String getAnnouncement(int id) {
        return Nova.getInstance().getConfig().getStringList("announcements").get(id);
    }

}
