package com.gameaurora.nova.utilities;

import org.bukkit.Location;

import com.gameaurora.nova.Nova;

public class SerializableLocation {

    private static String SEPARATOR = "@";

    public static Location stringToLocation(String string) {
        String[] split = string.split("@");
        return new Location(Nova.getInstance().getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + SEPARATOR + location.getX() + SEPARATOR + location.getY() + SEPARATOR + location.getZ() + SEPARATOR + location.getYaw() + SEPARATOR + location.getPitch();
    }

    public static Location simplify(Location location) {
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), 0F, 0F);
    }

    public static boolean compare(Location one, Location two) {
        return one.getWorld().getName().equals(two.getWorld().getName()) && one.getBlockX() == two.getBlockX() && one.getBlockY() == two.getBlockY() && one.getBlockZ() == two.getBlockZ();
    }

}
