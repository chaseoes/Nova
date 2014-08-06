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

import com.gameaurora.nova.events.listeners.RegionEventListeners;
import com.gameaurora.nova.general.listeners.PlayerJoinListener;
import com.gameaurora.nova.modules.adminmode.AdminModeCommand;
import com.gameaurora.nova.modules.adminmode.AdminModeListeners;
import com.gameaurora.nova.modules.adminmode.AdminModeTask;
import com.gameaurora.nova.modules.arrowtp.ArrowTeleportListeners;
import com.gameaurora.nova.modules.autosave.AutosaveTask;
import com.gameaurora.nova.modules.blockcommands.BlockCommandsCommands;
import com.gameaurora.nova.modules.blockcommands.BlockCommandsListeners;
import com.gameaurora.nova.modules.broadcast.BroadcastCommand;
import com.gameaurora.nova.modules.broadcastfirstjoin.FirstJoinListener;
import com.gameaurora.nova.modules.chatcommands.ChatCommands;
import com.gameaurora.nova.modules.farmworld.FarmworldCommands;
import com.gameaurora.nova.modules.farmworld.FarmworldListeners;
import com.gameaurora.nova.modules.giveitems.GiveItemsListener;
import com.gameaurora.nova.modules.guestrestrictions.RestrictionListener;
import com.gameaurora.nova.modules.hidestream.HideStreamListener;
import com.gameaurora.nova.modules.infocommands.InfoCommands;
import com.gameaurora.nova.modules.launchpads.PadCommands;
import com.gameaurora.nova.modules.launchpads.PadListener;
import com.gameaurora.nova.modules.lobbybuffs.BuffsListener;
import com.gameaurora.nova.modules.logger.LogListener;
import com.gameaurora.nova.modules.maintenancemode.MaintenanceModeTask;
import com.gameaurora.nova.modules.menu.MenuListeners;
import com.gameaurora.nova.modules.menu.ServerMenuCommand;
import com.gameaurora.nova.modules.nametags.ScoreboardListeners;
import com.gameaurora.nova.modules.portals.PortalListener;
import com.gameaurora.nova.modules.punch.PunchListener;
import com.gameaurora.nova.modules.signcolors.SignColorListener;
import com.gameaurora.nova.modules.signlinks.SignLinkListener;
import com.gameaurora.nova.modules.spawnonjoin.JoinSpawnListener;
import com.gameaurora.nova.modules.teleportcommands.TeleportCommand;
import com.gameaurora.nova.modules.teleportcommands.TeleportData;
import com.gameaurora.nova.modules.votifier.VoteListener;
import com.gameaurora.nova.utilities.DataConfiguration;
import com.gameaurora.nova.utilities.GeneralUtilities;
import com.gameaurora.nova.utilities.ModuleConfiguration;
import com.gameaurora.nova.utilities.PlayerStateStorage;
import com.gameaurora.nova.utilities.SerializableLocation;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

// TODO: getPlayer is deprecated. :(
@SuppressWarnings("deprecation")
public class Nova extends JavaPlugin {

    private static Nova instance;
    public List<String> adminPlayers = new ArrayList<String>();
    public HashMap<String, PlayerStateStorage> adminPlayerStates = new HashMap<String, PlayerStateStorage>();
    public TeleportData teleportData;
    public static Location LOBBY_LOCATION;
    private WorldGuardPlugin worldGuard;

    public EffectLib effectLib;
    public EffectManager effectManager;

    public static Nova getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        teleportData = new TeleportData();

        getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            }
        }, 60L);

        effectLib = EffectLib.instance();
        effectManager = new EffectManager(effectLib);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        ModuleConfiguration.reload();
        ModuleConfiguration.getConfig().options().copyDefaults(true);
        ModuleConfiguration.save();
        ModuleConfiguration.reload();

        loadGeneralModules();
        loadModules();

        loadLobbyLocation();

        DataConfiguration.reload();
        DataConfiguration.save();
    }

    public void onDisable() {
        for (String s : GeneralUtilities.makeCopy(adminPlayers)) {
            Player player = getServer().getPlayerExact(s);
            if (player != null) {
                String[] str = null;
                new AdminModeCommand().onCommand((CommandSender) player, getCommand("a"), "", str);
            }
        }

        getServer().getScheduler().cancelTasks(Nova.getInstance());
        instance = null;
    }

    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if (string.equalsIgnoreCase(getDescription().getName().toLowerCase())) {
            if (strings.length == 0) {
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + getDescription().getName() + " version " + ChatColor.GREEN + getDescription().getVersion() + ChatColor.GRAY + " by chaseoes.");
                cs.sendMessage(NovaMessages.PREFIX_GENERAL + "http://gameaurora.com");
                return true;
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

    public void teleportToLobby(Player player) {
        if (getServer().getWorld("lobby") != null) {
            player.teleport(getServer().getWorld("lobby").getSpawnLocation());
        }
    }

    private void loadGeneralModules() {
        PluginManager pm = getServer().getPluginManager();

        // General Events
        pm.registerEvents(new PlayerJoinListener(), this);

        // Chat Commands
        getCommand("clearchat").setExecutor(new ChatCommands());

        // Sign Colors
        pm.registerEvents(new SignColorListener(), this);

        // Server Menu
        pm.registerEvents(new MenuListeners(), this);
        getCommand("s").setExecutor(new ServerMenuCommand());

        // Sign Links
        pm.registerEvents(new SignLinkListener(), this);

        // Punch
        pm.registerEvents(new PunchListener(), this);

        // Block Commands
        getCommand("bc").setExecutor(new BlockCommandsCommands());
        pm.registerEvents(new BlockCommandsListeners(), this);

        // Nametags
        pm.registerEvents(new ScoreboardListeners(), this);

        // Broadcast
        getCommand("broadcast").setExecutor(new BroadcastCommand());

        // Info Commands
        getCommand("help").setExecutor(new InfoCommands());
        getCommand("vote").setExecutor(new InfoCommands());
        getCommand("member").setExecutor(new InfoCommands());
        getCommand("rules").setExecutor(new InfoCommands());
        getCommand("tos").setExecutor(new InfoCommands());
        getCommand("privacy").setExecutor(new InfoCommands());
        getCommand("livemap").setExecutor(new InfoCommands());
        getCommand("store").setExecutor(new InfoCommands());
        getCommand("mumble").setExecutor(new InfoCommands());

    }

    private void loadModules() {
        PluginManager pm = getServer().getPluginManager();
        if (getModule("logger").isEnabled()) {
            pm.registerEvents(new LogListener(), this);
        }

        if (getModule("launch-pads").isEnabled()) {
            pm.registerEvents(new PadListener(), this);
            getCommand("launchpad").setExecutor(new PadCommands());
        }

        if (getModule("teleport-commands").isEnabled()) {
            getCommand("teleport").setExecutor(new TeleportCommand());
            getCommand("teleporthere").setExecutor(new TeleportCommand());
            getCommand("teleportaccept").setExecutor(new TeleportCommand());
            getCommand("teleporttoggle").setExecutor(new TeleportCommand());
        }

        if (getModule("portals").isEnabled()) {
            pm.registerEvents(new RegionEventListeners(), this);
            pm.registerEvents(new PortalListener(), this);
        }

        if (getModule("spawn-on-join").isEnabled()) {
            pm.registerEvents(new JoinSpawnListener(), this);
        }

        if (getModule("hide-stream").isEnabled()) {
            pm.registerEvents(new HideStreamListener(), this);
        }

        if (getModule("auto-save").isEnabled()) {
            getServer().getScheduler().runTaskTimer(this, new AutosaveTask(), 6000L, 6000L);
        }

        if (getModule("admin-mode").isEnabled()) {
            getServer().getScheduler().runTaskTimer(this, new AdminModeTask(), 0L, 20L);
            getCommand("adminmode").setExecutor(new AdminModeCommand());
            pm.registerEvents(new AdminModeListeners(), this);
        }

        if (getModule("give-items").isEnabled()) {
            pm.registerEvents(new GiveItemsListener(), this);

            if (getModule("give-items").getConfig().getStringList("items").contains("TELEPORT_BOW")) {
                pm.registerEvents(new ArrowTeleportListeners(), this);
            }
        }

        if (getModule("lobby-buffs").isEnabled()) {
            pm.registerEvents(new BuffsListener(), this);
        }

        if (getModule("broadcast-first-join").isEnabled()) {
            pm.registerEvents(new FirstJoinListener(), this);
        }

        if (getModule("votifier").isEnabled()) {
            pm.registerEvents(new VoteListener(), this);
        }

        if (getModule("maintenance-mode").isEnabled()) {
            getServer().getScheduler().runTaskTimer(this, new MaintenanceModeTask(), 0L, 3600L);
        }

        if (getModule("farmworld").isEnabled()) {
            pm.registerEvents(new FarmworldListeners(), this);
            getCommand("farmworld").setExecutor(new FarmworldCommands());
        }

        if (getModule("guest-restrictions").isEnabled()) {
            pm.registerEvents(new RestrictionListener(), this);
        }

    }

    private void loadLobbyLocation() {
        LOBBY_LOCATION = new Location(getInstance().getServer().getWorld("lobby"), 0.5, 65, 0.5, -180, 0);
    }

    public WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }

    public NovaModule getModule(String moduleName) {
        return new NovaModule(moduleName);
    }

}
