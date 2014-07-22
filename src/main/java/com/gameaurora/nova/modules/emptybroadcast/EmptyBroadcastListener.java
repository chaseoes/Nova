package com.gameaurora.nova.modules.emptybroadcast;

import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.utilities.GeneralUtilities;

public class EmptyBroadcastListener implements Listener {

    public static long tracker = 0;

    @EventHandler
    public void onPlayerJoin2(PlayerJoinEvent event) {
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(tracker);
        long currentTimeSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long diffSeconds = currentTimeSeconds - timeSeconds;

        if (diffSeconds > 600 || tracker == 0) {
            tracker = System.currentTimeMillis();
            if (Nova.getInstance().getServer().getOnlinePlayers().length == 1) {
                Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), "broadcast " + GeneralUtilities.getPrettyServerName() + " needs a couple more people! Come play? :D");
            }
        }
    }

}
