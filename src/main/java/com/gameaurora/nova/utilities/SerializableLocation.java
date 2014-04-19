package com.gameaurora.nova.utilities;

import org.bukkit.Location;
import org.bukkit.World;

import com.gameaurora.nova.Nova;

public class SerializableLocation {

    public static String locationToString(Location l) {
        String w = l.getWorld().getName();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        float pitch = l.getPitch();
        float yaw = l.getYaw();
        return w + "." + x + "." + y + "." + z + "." + yaw + "." + pitch;
    }

    public static Location stringToLocation(String s) {
        String[] str = s.split("\\.");
        World w = Nova.getInstance().getServer().getWorld(str[0]);
        int x = Integer.parseInt(str[1]);
        int y = Integer.parseInt(str[2]);
        int z = Integer.parseInt(str[3]);
        float yaw = Integer.parseInt(str[4]);
        float pitch = Integer.parseInt(str[5]);
        return new Location(w, x, y, z, yaw, pitch);
    }

    public static boolean compareLocations(Location one, Location two) {
        String w = one.getWorld().getName();
        int x = one.getBlockX();
        int y = one.getBlockY();
        int z = one.getBlockZ();

        String checkw = two.getWorld().getName();
        int checkx = two.getBlockX();
        int checky = two.getBlockY();
        int checkz = two.getBlockZ();

        return w.equalsIgnoreCase(checkw) && x == checkx && y == checky && z == checkz;
    }

}
