package com.gameaurora.nova.modules.autosave;

import org.bukkit.scheduler.BukkitRunnable;

import com.gameaurora.nova.Nova;

public class AutosaveTask extends BukkitRunnable {

    public void run() {
        Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), "save-all");
        Nova.getInstance().getServer().savePlayers();
    }

}
