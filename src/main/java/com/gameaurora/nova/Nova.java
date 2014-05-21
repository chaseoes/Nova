package com.gameaurora.nova;

import com.gameaurora.modules.autosave.AutosaveTask;
import com.gameaurora.modules.bans.BanCommand;
import com.gameaurora.modules.bans.BanUtilities;
import com.gameaurora.modules.bans.BansListener;
import com.gameaurora.modules.bans.IsBannedCommand;
import com.gameaurora.modules.bans.UnbanCommand;
import com.gameaurora.nova.events.listeners.RegionEventListeners;
import com.gameaurora.nova.modules.adminmode.AdminModeCommand;
import com.gameaurora.nova.modules.adminmode.AdminModeListeners;
import com.gameaurora.nova.modules.adminmode.AdminModeTask;
import com.gameaurora.nova.modules.arrowtp.ArrowTeleportListeners;
import com.gameaurora.nova.modules.chat.ChatData;
import com.gameaurora.nova.modules.chat.ChatListeners;
import com.gameaurora.nova.modules.hidestream.HideStreamListener;
import com.gameaurora.nova.modules.hubcommand.HubCommand;
import com.gameaurora.nova.modules.joinspawn.JoinSpawnListener;
import com.gameaurora.nova.modules.kick.KickCommand;
import com.gameaurora.nova.modules.logger.LogListener;
import com.gameaurora.nova.modules.menu.MenuListeners;
import com.gameaurora.nova.modules.menu.MenuUtilities;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;
import com.gameaurora.nova.modules.nametags.ServerScoreboard;
import com.gameaurora.nova.modules.onlyproxyjoin.OnlyProxyJoinListener;
import com.gameaurora.nova.modules.pads.PadCommands;
import com.gameaurora.nova.modules.pads.PadListener;
import com.gameaurora.nova.modules.portals.PortalListener;
import com.gameaurora.nova.modules.punch.PunchListener;
import com.gameaurora.nova.modules.signcolors.SignColorListener;
import com.gameaurora.nova.modules.signlinks.SignLinkListener;
import com.gameaurora.nova.modules.superspeed.SuperSpeedListener;
import com.gameaurora.nova.modules.teleport.TeleportCommand;
import com.gameaurora.nova.modules.teleport.TeleportData;
import com.gameaurora.nova.utilities.PlayerStateStorage;
import com.gameaurora.nova.utilities.SQLUtilities;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nova extends JavaPlugin implements PluginMessageListener {

	private static Nova instance;
	public List<String> adminPlayers = new ArrayList<String>();
	public HashMap<String, PlayerStateStorage> adminPlayerStates = new HashMap<String, PlayerStateStorage>();
	public TeleportData teleportData;
	public ChatData chatData;
	private SQLUtilities sql;
	public static Location LOBBY_LOCATION;
	public static final String CHANNEL_SEPARATOR = "AABBCCDDEEZ";

	public static Nova getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		teleportData = new TeleportData();
		chatData = new ChatData();

		if (moduleIsEnabled("bans")) {
			sql = new SQLUtilities(this);
		}

		loadModules();
		getServer().getPluginManager().registerEvents(new RegionEventListeners(), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		loadLobbyLocation();

		for (String server : MenuUtilities.icons.keySet()) {
			PlayerCountUtilities.requestPlayerCount(server);
		}
	}

	public void onReload() {
		for (String s : adminPlayers) {
			Player player = getServer().getPlayerExact(s);
			if (player != null) {
				String[] str = null;
				new AdminModeCommand().onCommand((CommandSender) player, getCommand("a"), "", str);
			}
		}

		chatData.reloadProfiles();
		getServer().getScheduler().cancelTasks(Nova.getInstance());

		loadLobbyLocation();
	}

	public void onDisable() {
		for (String s : adminPlayers) {
			Player player = getServer().getPlayerExact(s);
			if (player != null) {
				String[] str = null;
				new AdminModeCommand().onCommand((CommandSender) player, getCommand("a"), "", str);
			}
		}

		getServer().getScheduler().cancelTasks(Nova.getInstance());

		if (sql != null) {
			sql.close();
		}

		instance = null;
	}

	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
		if (string.equalsIgnoreCase(getDescription().getName().toLowerCase())) {
			if (strings.length == 0) {
				cs.sendMessage(NovaMessages.PREFIX_GENERAL + getDescription().getName() + " version " + ChatColor.GREEN + getDescription().getVersion() + ChatColor.GRAY + " by chaseoes.");
				cs.sendMessage(NovaMessages.PREFIX_GENERAL + "http://gameaurora.com");
				return true;
			}
		} else {
			cs.sendMessage(NovaMessages.PREFIX_ERROR + "That feature isn't enabled on this server.");
		}
		return true;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		try {
			String subchannel = in.readUTF();
			if (subchannel.equals("PlayerCount")) {
				try {
					String server = in.readUTF();
					int playerCount = in.readInt();
					PlayerCountUtilities.setPlayerCount(server, playerCount);
				} catch (Exception e) {

				}
				return;
			}

			if (subchannel.equals("NovaChatMessage")) {
				short len = in.readShort();
				byte[] msgbytes = new byte[len];
				in.readFully(msgbytes);
				DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));

				String serverName = msgin.readUTF();
				String playerName = msgin.readUTF();
				String chatFormat = msgin.readUTF();
				String chatMessage = msgin.readUTF();

				String finalMessage = String.format(chatFormat, playerName, chatMessage);
				FancyMessage fancyMessage = new FancyMessage(finalMessage).tooltip(ChatColor.GREEN + "Current Server: " + ChatColor.AQUA + serverName);

				for (Player onlinePlayer : getServer().getOnlinePlayers()) {
					onlinePlayer.sendMessage(fancyMessage.toJSONString());
					fancyMessage.send(onlinePlayer);
				}
				return;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
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
			getCommand("lp").setExecutor(new PadCommands());
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

		if (moduleIsEnabled("adminmode")) {
			getServer().getScheduler().runTaskTimer(this, new AdminModeTask(), 0L, 20L);
			getCommand("adminmode").setExecutor(new AdminModeCommand());
			pm.registerEvents(new AdminModeListeners(), this);
		}

		if (moduleIsEnabled("bans")) {
			BanUtilities.getInstance();
			getCommand("ban").setExecutor(new BanCommand());
			getCommand("unban").setExecutor(new UnbanCommand());
			getCommand("isbanned").setExecutor(new IsBannedCommand());
			getServer().getPluginManager().registerEvents(new BansListener(), this);
		}

		if (moduleIsEnabled("menu")) {
			MenuUtilities.loadIcons();
			pm.registerEvents(new MenuListeners(), this);
		}

		if (moduleIsEnabled("signlinks")) {
			pm.registerEvents(new SignLinkListener(), this);
		}

		if (moduleIsEnabled("punch")) {
			pm.registerEvents(new PunchListener(), this);
		}

		if (moduleIsEnabled("arrowtp")) {
			pm.registerEvents(new ArrowTeleportListeners(), this);
		}

		if (moduleIsEnabled("nametags")) {
			ServerScoreboard.clear();
			ServerScoreboard.load();
			ServerScoreboard.updateBoard();
		}

		if (moduleIsEnabled("superspeed")) {
			pm.registerEvents(new SuperSpeedListener(), this);
		}

		if (moduleIsEnabled("kick")) {
			getCommand("kick").setExecutor(new KickCommand());
		}
	}

	public SQLUtilities getSQL() {
		return sql;
	}

	private void loadLobbyLocation() {
		LOBBY_LOCATION = new Location(getInstance().getServer().getWorld("lobby"), 0.5, 65, 0.5, -180, 0);
	}

}
