package com.gameaurora.nova.modules.commandshortcuts;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.gameaurora.nova.Nova;

public class CommandShortcutListener implements Listener {

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		ConfigurationSection shortcuts = Nova.getInstance().getConfig().getConfigurationSection("command-shortcuts");
		if (shortcuts != null) {
			for (String preCommand : shortcuts.getKeys(false)) {
				String command = shortcuts.getString(preCommand);
				if (event.getMessage().equals("/" + preCommand)) {
					event.getPlayer().performCommand(command);
					event.setCancelled(true);
				}
			}
		}
	}

}
