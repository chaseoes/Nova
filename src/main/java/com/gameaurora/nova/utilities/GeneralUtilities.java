package com.gameaurora.nova.utilities;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.gameaurora.nova.Nova;
import com.gameaurora.nova.NovaMessages;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class GeneralUtilities {

	public static void launchRandomFirework(Location location) {
		Random random = new Random();
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = fw.getFireworkMeta();

		meta.setPower(1 + random.nextInt(4));
		FireworkEffect.Builder builder = FireworkEffect.builder().
				trail(random.nextBoolean()).
				flicker(random.nextBoolean());

		builder.with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]);
		Set<Color> colors = new HashSet<Color>();
		for (int i = 0; i < 3; i++) {
			colors.add(Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		}

		builder.withColor(colors);
		meta.addEffect(builder.build());
		fw.setFireworkMeta(meta);
	}

	public static void sendToServer(Player player, String server) {
		player.sendMessage(NovaMessages.CHANGE_SERVER);
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(Nova.getInstance(), "BungeeCord", out.toByteArray());
	}
	
	public static void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
	}

}
