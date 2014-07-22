package com.gameaurora.nova.modules.lagtest;

import java.util.Calendar;
import java.util.logging.Level;

import org.bukkit.scheduler.BukkitRunnable;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.modules.logger.LogEntryType;
import com.gameaurora.nova.modules.logger.LoggerUtilities;

public class LagTestTask extends BukkitRunnable {

    private static int lastSecond = 0;

    public void run() {
        Calendar now = Calendar.getInstance();
        int seconds = now.get(Calendar.SECOND);
        Nova.getInstance().getLogger().log(Level.WARNING, "Lag task running. Current system time: " + seconds);

        int diff = (seconds - lastSecond);
        if (diff > 3) {
            LoggerUtilities.log("Lag? " + diff, LogEntryType.HOPPER);
        }
        lastSecond = seconds;
    }

}
