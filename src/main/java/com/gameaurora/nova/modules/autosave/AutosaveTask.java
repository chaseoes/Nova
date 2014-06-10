package com.gameaurora.nova.modules.autosave;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import com.gameaurora.nova.Nova;

public class AutosaveTask extends BukkitRunnable {

	public void run() {
		DateFormat dateFormat = new SimpleDateFormat("m");
		Date date = new Date();
		if (dateFormat.format(date).endsWith("0") || dateFormat.format(date).endsWith("5")) {
			Nova.getInstance().getServer().dispatchCommand(Nova.getInstance().getServer().getConsoleSender(), "save-all");
			Nova.getInstance().getServer().savePlayers();
		}
	}

}
