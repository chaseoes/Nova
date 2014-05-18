package com.gameaurora.modules.bans;

import com.gameaurora.nova.Nova;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BanUtilities {

	private final HashMap<UUID, String> bans = new HashMap<UUID, String>();
	private static BanUtilities instance = new BanUtilities();

	private BanUtilities() {
		BansSql.createTables();
		Bukkit.getScheduler().runTaskTimerAsynchronously(Nova.getInstance(), new Runnable() {
			@Override
			public void run() {
				BansSql.syncBans(bans);
				Bukkit.getScheduler().runTask(Nova.getInstance(), new Runnable() {
					@Override
					public void run() {
						banAfterSync();
					}
				});
			}
		}, 0L, 20 * 60L);
	}

	private void banAfterSync() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (isBanned(player.getUniqueId())) {
				player.kickPlayer(getBanReason(player.getUniqueId()));
			}
		}
	}

	public static BanUtilities getInstance() {
		return instance;
	}

	public void banPlayer(final UUID player, final String reason, final Runnable successCallback, final Runnable errorCallback) {
		synchronized (bans) {
			bans.put(player, reason);
		}
		Bukkit.getScheduler().runTaskAsynchronously(Nova.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (BansSql.banPlayer(player, reason)) {
					Bukkit.getScheduler().runTask(Nova.getInstance(), successCallback);
				} else {
					Bukkit.getScheduler().runTask(Nova.getInstance(), errorCallback);
				}

			}
		});
	}

	public void unbanPlayer(final UUID player, final Runnable successCallback, final Runnable errorCallback) {
		synchronized (bans) {
			bans.remove(player);
		}
		Bukkit.getScheduler().runTaskAsynchronously(Nova.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (BansSql.unbanPlayer(player)) {
					Bukkit.getScheduler().runTask(Nova.getInstance(), successCallback);
				} else {
					Bukkit.getScheduler().runTask(Nova.getInstance(), errorCallback);
				}
			}
		});
	}

	public boolean isBanned(UUID player) {
		synchronized (bans) {
			return bans.containsKey(player);
		}
	}

	public String getBanReason(UUID player) {
		synchronized (bans) {
			return bans.get(player);
		}
	}

}
