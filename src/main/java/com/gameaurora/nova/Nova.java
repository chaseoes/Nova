package com.gameaurora.nova;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

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
import com.gameaurora.nova.modules.announcer.AnnouncerTask;
import com.gameaurora.nova.modules.arrowtp.ArrowTeleportListeners;
import com.gameaurora.nova.modules.blockcommands.BlockCommandsCommands;
import com.gameaurora.nova.modules.blockcommands.BlockCommandsListeners;
import com.gameaurora.nova.modules.chat.ChatData;
import com.gameaurora.nova.modules.chat.ChatListeners;
import com.gameaurora.nova.modules.cloudmessages.CloudMessage;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageListeners;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageReceiveListener;
import com.gameaurora.nova.modules.cloudmessages.CloudMessageType;
import com.gameaurora.nova.modules.commandshortcuts.CommandShortcutListener;
import com.gameaurora.nova.modules.hidestream.HideStreamListener;
import com.gameaurora.nova.modules.hubcommand.HubCommand;
import com.gameaurora.nova.modules.joinspawn.JoinSpawnListener;
import com.gameaurora.nova.modules.kick.KickCommand;
import com.gameaurora.nova.modules.logger.LogListener;
import com.gameaurora.nova.modules.maintenancemode.MaintenanceModeTask;
import com.gameaurora.nova.modules.menu.MenuListeners;
import com.gameaurora.nova.modules.menu.MenuUtilities;
import com.gameaurora.nova.modules.menu.PlayerCountUtilities;
import com.gameaurora.nova.modules.menu.ServerMenuCommand;
import com.gameaurora.nova.modules.nametags.ServerScoreboard;
import com.gameaurora.nova.modules.onlyproxyjoin.OnlyProxyJoinListener;
import com.gameaurora.nova.modules.pads.PadCommands;
import com.gameaurora.nova.modules.pads.PadListener;
import com.gameaurora.nova.modules.pingchat.PingChatListener;
import com.gameaurora.nova.modules.portals.PortalListener;
import com.gameaurora.nova.modules.punch.PunchListener;
import com.gameaurora.nova.modules.signcolors.SignColorListener;
import com.gameaurora.nova.modules.signlinks.SignLinkListener;
import com.gameaurora.nova.modules.superspeed.SuperSpeedListener;
import com.gameaurora.nova.modules.teleport.TeleportCommand;
import com.gameaurora.nova.modules.teleport.TeleportData;
import com.gameaurora.nova.modules.tokens.TokenCommands;
import com.gameaurora.nova.modules.tokens.TokenData;
import com.gameaurora.nova.modules.tokens.TokenDataFile;
import com.gameaurora.nova.modules.tokens.TokenListeners;
import com.gameaurora.nova.utilities.GeneralUtilities;
import com.gameaurora.nova.utilities.PlayerStateStorage;
import com.gameaurora.nova.utilities.SQLUtilities;
import com.gameaurora.nova.utilities.SerializableLocation;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Nova extends JavaPlugin {

    private static Nova instance;
    public List<String> adminPlayers = new ArrayList<String>();
    public HashMap<String, PlayerStateStorage> adminPlayerStates = new HashMap<String, PlayerStateStorage>();
    public TeleportData teleportData;
    public ChatData chatData;
    private SQLUtilities sql;
    public static Location LOBBY_LOCATION;
    public static final String CHANNEL_SEPARATOR = "AABBCCDDEEZ";
    private WorldGuardPlugin worldGuard;

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

        getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            }
        }, 60L);

        getServer().getPluginManager().registerEvents(new RegionEventListeners(), this);
        getServer().getPluginManager().registerEvents(new CloudMessageListeners(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new CloudMessageReceiveListener());
        loadModules();

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

            if (strings[0].equalsIgnoreCase("send")) {
                CloudMessage message = new CloudMessage("TEST MESSAGE!", CloudMessageType.CHAT.toString(), GeneralUtilities.getServerName(), GeneralUtilities.getPrettyServerName());
                message.send();
                cs.sendMessage("SENDING...");
            }

            if (strings[0].equalsIgnoreCase("getpos")) {
                if (cs.hasPermission("nova.getpos")) {
                    if (cs instanceof Player) {
                        Location location = ((Player) cs).getLocation();
                        cs.sendMessage(NovaMessages.PREFIX_GENERAL + "Your current location information:");
                        cs.sendMessage(ChatColor.GRAY + "X: " + ChatColor.GREEN + location.getBlockX());
                        cs.sendMessage(ChatColor.GRAY + "Y: " + ChatColor.GREEN + location.getBlockY());
                        cs.sendMessage(ChatColor.GRAY + "Z: " + ChatColor.GREEN + location.getBlockZ());
                        cs.sendMessage(ChatColor.GRAY + "Yaw: " + ChatColor.GREEN + location.getYaw());
                        cs.sendMessage(ChatColor.GRAY + "Pitch: " + ChatColor.GREEN + location.getPitch());
                        cs.sendMessage(ChatColor.GRAY + "Location String: " + ChatColor.GREEN + SerializableLocation.locationToString(location));
                    } else {
                        cs.sendMessage(NovaMessages.MUST_BE_PLAYER);
                    }
                } else {
                    cs.sendMessage(NovaMessages.NO_PERMISSION);
                }
            }
        } else {
            cs.sendMessage(NovaMessages.PREFIX_ERROR + "That feature isn't enabled on this server.");
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
            getCommand("s").setExecutor(new ServerMenuCommand());
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

        if (moduleIsEnabled("tokens")) {
            TokenDataFile.reloadDataFile();
            TokenData.load();
            getCommand("tokens").setExecutor(new TokenCommands());
            pm.registerEvents(new TokenListeners(), this);
        }

        if (moduleIsEnabled("superspeed")) {
            pm.registerEvents(new SuperSpeedListener(), this);
        }

        if (moduleIsEnabled("kick")) {
            getCommand("kick").setExecutor(new KickCommand());
        }

        if (moduleIsEnabled("blockcommands")) {
            getCommand("bc").setExecutor(new BlockCommandsCommands());
            pm.registerEvents(new BlockCommandsListeners(), this);
        }

        if (moduleIsEnabled("maintenancemode")) {
            getServer().getScheduler().runTaskTimer(this, new MaintenanceModeTask(), 0L, 3600L);
        }

        if (moduleIsEnabled("pingchat")) {
            pm.registerEvents(new PingChatListener(), this);
        }

        if (moduleIsEnabled("commandshortcuts")) {
            pm.registerEvents(new CommandShortcutListener(), this);
        }

        if (moduleIsEnabled("nametags")) {
            for (Player player : getServer().getOnlinePlayers()) {
                ServerScoreboard board = new ServerScoreboard(player);
                board.updateBoard();
            }
        }

        if (moduleIsEnabled("announcer")) {
            getServer().getScheduler().runTaskTimer(this, new AnnouncerTask(), 0L, 5L * 60L * 20L);
        }
    }

    public SQLUtilities getSQL() {
        return sql;
    }

    private void loadLobbyLocation() {
        LOBBY_LOCATION = new Location(getInstance().getServer().getWorld("lobby"), 0.5, 65, 0.5, -180, 0);
    }

    public void updateScoreboard(Player player) {
        ServerScoreboard board = new ServerScoreboard(player);
        board.updateBoard();
    }

    public WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }

}
