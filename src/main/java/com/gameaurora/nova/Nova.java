package com.gameaurora.nova;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gameaurora.modules.autosave.AutosaveTask;
import com.gameaurora.nova.events.listeners.RegionEventListeners;
import com.gameaurora.nova.modules.chat.ChatData;
import com.gameaurora.nova.modules.chat.ChatListeners;
import com.gameaurora.nova.modules.hidestream.HideStreamListener;
import com.gameaurora.nova.modules.hubcommand.HubCommand;
import com.gameaurora.nova.modules.joinspawn.JoinSpawnListener;
import com.gameaurora.nova.modules.logger.LogListener;
import com.gameaurora.nova.modules.onlyproxyjoin.OnlyProxyJoinListener;
import com.gameaurora.nova.modules.pads.PadListener;
import com.gameaurora.nova.modules.permissions.PermissionCommands;
import com.gameaurora.nova.modules.permissions.PermissionUtilities;
import com.gameaurora.nova.modules.portals.PortalListener;
import com.gameaurora.nova.modules.signcolors.SignColorListener;
import com.gameaurora.nova.modules.teleport.TeleportCommand;
import com.gameaurora.nova.modules.teleport.TeleportData;

public class Nova extends JavaPlugin {

	private static Nova instance;
	public TeleportData teleportData;
	public ChatData chatData;

	public static Nova getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		teleportData = new TeleportData();
		chatData = new ChatData();
		loadModules();
		getServer().getPluginManager().registerEvents(new RegionEventListeners(), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	public void onReload() {
		chatData.reloadProfiles();
		getServer().getScheduler().cancelTasks(Nova.getInstance());
	}

	public void onDisable() {
		getServer().getScheduler().cancelTasks(Nova.getInstance());
		PermissionUtilities.getUtilities().onDisable();
		instance = null;
	}

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (strings.length == 0) {
			cs.sendMessage(NovaMessages.PREFIX_GENERAL + getDescription().getName() + " version " + getDescription().getVersion() + " by chaseoes.");
			cs.sendMessage(NovaMessages.PREFIX_GENERAL + "http://emeraldsmc.com");
			return true;
		}

		if (strings[0].equalsIgnoreCase("reload")) {
			reloadConfig();
			saveConfig();
			loadModules();
			cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Successfully reloaded configuration and modules.");
			return true;
		}
		return true;
	}

	public boolean moduleIsEnabled(String name) {
		return getConfig().getStringList("modules").contains(name);
	}

	public void teleportToLobby(Player player) {
		if (getServer().getWorld("lobby") != null) {
			player.teleport(getServer().getWorld("lobby").getSpawnLocation());
		}
	}

	private void loadModules() {
		PluginManager pm = getServer().getPluginManager();
		if (moduleIsEnabled("logger")) {
			pm.registerEvents(new LogListener(), this);
		}

		if (moduleIsEnabled("pads")) {
			pm.registerEvents(new PadListener(), this);
		}

		if (moduleIsEnabled("teleport")) {
			getCommand("teleport").setExecutor(new TeleportCommand());
			getCommand("teleporthere").setExecutor(new TeleportCommand());
			getCommand("teleportaccept").setExecutor(new TeleportCommand());
			getCommand("teleportdeny").setExecutor(new TeleportCommand());
			getCommand("teleporttoggle").setExecutor(new TeleportCommand());
		}

		if (moduleIsEnabled("portals")) {
			pm.registerEvents(new PortalListener(), this);
		}

		if (moduleIsEnabled("permissions")) {
			getCommand("permissions").setExecutor(new PermissionCommands());
			PermissionUtilities.getUtilities().onEnable();
		}

		if (moduleIsEnabled("chat")) {
			chatData.reloadProfiles();
			pm.registerEvents(new ChatListeners(), this);
		}

		if (moduleIsEnabled("signcolors")) {
			pm.registerEvents(new SignColorListener(), this);
		}

		if (moduleIsEnabled("joinspawn")) {
			pm.registerEvents(new JoinSpawnListener(), this);
		}

		if (moduleIsEnabled("onlyproxyjoin")) {
			pm.registerEvents(new OnlyProxyJoinListener(), this);
		}

		if (moduleIsEnabled("hubcommand")) {
			getCommand("hub").setExecutor(new HubCommand());
		}

		if (moduleIsEnabled("hidestream")) {
			pm.registerEvents(new HideStreamListener(), this);
		}

		if (moduleIsEnabled("autosave")) {
			getServer().getScheduler().runTaskTimer(this, new AutosaveTask(), 0L, 1200L);
		}
	}

}
